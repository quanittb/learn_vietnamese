package com.mobiai.app.ui.fragment

import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
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


    // Hàm thay đổi mật khẩu
    private fun changePassword(newPassword: String, onComplete: (Boolean, String) -> Unit) {
        val user = FirebaseAuth.getInstance().currentUser

        // Kiểm tra xem người dùng đã đăng nhập chưa
        if (user != null) {
            user.updatePassword(newPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        onComplete(true, "Mật khẩu đã được thay đổi thành công")
                    } else {
                        onComplete(false, "Lỗi khi thay đổi mật khẩu: ${task.exception?.message}")
                    }
                }
        } else {
            onComplete(false, "Người dùng chưa đăng nhập")
        }
    }


    private fun changePass(){
        val passwordOld = binding.inputOldPass.text.toString().trim()
        val passwordNew = binding.inputNewPass.text.toString().trim()
        if (isValidSignInDetails()){
            changePassword("Mật khẩu mới") { success, message ->
                if (success) {
                    // Xử lý khi thay đổi mật khẩu thành công
                    Log.d(TAG, "changePass:1 $message")
                } else {
                    Log.d(TAG, "changePass: 2 $message")

                    // Xử lý khi thay đổi mật khẩu thất bại
                }
            }
            // SharedPreferenceUtils.emailLogin?.let { fireBasePasswordChange(it,passwordOld,passwordNew) }
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