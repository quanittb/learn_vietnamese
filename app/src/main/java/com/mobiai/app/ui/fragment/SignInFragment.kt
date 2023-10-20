package com.mobiai.app.ui.fragment

import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mobiai.app.model.User
import com.mobiai.app.ui.fragment.SignUpFragment.Companion.EMAIL
import com.mobiai.base.basecode.ui.fragment.BaseFragment
import com.mobiai.databinding.LoginFragmentBinding


class SignInFragment : BaseFragment<LoginFragmentBinding>() {

    companion object{
        fun instance() : SignInFragment{
            return newInstance(SignInFragment::class.java)
        }
    }
    override fun initView() {
        binding.tvSignUp.setOnClickListener {
            replaceFragment(SignUpFragment.instance())
        }

        binding.tvForgotPass.setOnClickListener {
            addFragment(ForgotPasswordFragment.instance())
        }
        binding.btnSignIn.setOnClickListener {
            if (isValidSignInDetails()){
                SignIn()
            }
        }
    }

    private fun SignIn(){
        val email = binding.inputEmail.text.toString().trim()
        val password = binding.inputPass.text.toString().trim()
        val db = FirebaseDatabase.getInstance()
        val ref = db.getReference(SignUpFragment.USER)
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (userSnapshot in dataSnapshot.children) {
                    // Lấy dữ liệu từ mỗi child node và chuyển đổi thành đối tượng User
                    val user = userSnapshot.getValue(User::class.java)
                    if (user != null) {
                        if (user.email == email && user.pass == password) {
                            replaceFragment(BottomNavigationFragment.instance())
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
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
    private fun isValidSignInDetails(): Boolean {
        // email trống
        return if (binding.inputEmail.text.toString().trim().isEmpty()) {
            showToast("Enter email")
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.text.toString().trim()).matches()) {
            showToast("Enter valid email")
            false
        } else if (binding.inputPass.text.toString().trim().isEmpty()) {
            showToast("Enter password")
            false
        }/* else if (dem >= 2) {
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
    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): LoginFragmentBinding {
        return LoginFragmentBinding.inflate(inflater, container,false)
    }
}