package com.mobiai.app.ui.fragment

import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.mobiai.app.model.User
import com.mobiai.app.utils.gone
import com.mobiai.app.utils.visible
import com.mobiai.base.basecode.storage.SharedPreferenceUtils
import com.mobiai.base.basecode.ui.fragment.BaseFragment
import com.mobiai.databinding.ChangePasswordFragmentBinding
import com.mobiai.databinding.ForgotPasswordFragmentBinding
import com.mobiai.databinding.SignUpFragmentBinding

class ChangePasswordFragment : BaseFragment<ChangePasswordFragmentBinding>() {

    companion object{
        fun instance() : ChangePasswordFragment{
            return newInstance(ChangePasswordFragment::class.java)
        }
    }
    val TAG = ChangePasswordFragment::class.java.name
    override fun initView() {
        binding.icBack.setOnClickListener {
            handlerBackPressed()
        }
        binding.btnChangePassword.setOnClickListener {
            changePass()
        }

    }

    private fun changePass(){
        val passwordOld = binding.inputOldPass.text.toString().trim()
        val passwordNew = binding.inputNewPass.text.toString().trim()
        if (isValidSignInDetails()){
            val db = FirebaseDatabase.getInstance()
            val ref = db.getReference(SignUpFragment.USER)
            ref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    for (userSnapshot in dataSnapshot.children) {
                        // Lấy dữ liệu từ mỗi child node và chuyển đổi thành đối tượng User
                        val user = userSnapshot.getValue(User::class.java)
                        if (user != null) {
                            if (user.email == SharedPreferenceUtils.emailLogin && user.pass == passwordOld) {
                                binding.frLoading.visible()
                                val userUpdate = User(SharedPreferenceUtils.emailLogin!!,user.name,passwordNew)
                                userSnapshot.key?.let { ref.child(it).setValue(userUpdate) }
                                showToast("change password success!")
                                val fragmentManager = requireActivity().supportFragmentManager
                                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                                addFragment(BottomNavigationFragment.instance())
                                binding.frLoading.gone()
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                    Log.w("TAG", "Failed to read value.", error.toException())
                }
            })
        }


    }
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
    private fun isValidSignInDetails(): Boolean {
        // email trống
        return if (binding.inputOldPass.text.toString().trim().isEmpty()) {
            showToast("Enter Old Pass")
            false
        }else if (binding.inputNewPass.text.toString().trim().isEmpty()) {
            showToast("Enter New password")
            false
        }else if (binding.inputEnterPass.text.toString().trim().isEmpty()) {
            showToast("Enter New password")
            false
        }else if (binding.inputNewPass.text.toString().trim() != binding.inputEnterPass.text.toString().trim()) {
            showToast("Enter not same New password")
            false
        }

        /* else if (dem >= 2) {
            showToast("wrong! wait 10 seconds,please ")
            binding.buttonSignIn.setVisibility(View.GONE)
            binding.progressBar.setVisibility(View.VISIBLE)
            val handler = Handler()
            handler.postDelayed(Runnable {
                binding.buttonSignIn.setVisibility(View.VISIBLE)
                binding.progressBar.setVisibility(View.GONE)
                dem = 0
            }, 10000)
            false
        }*/ else {
            true
        }
    }
    override fun handlerBackPressed() {
        super.handlerBackPressed()
        closeFragment(this)
    }
    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ChangePasswordFragmentBinding {
        return ChangePasswordFragmentBinding.inflate(inflater, container,false)
    }
}