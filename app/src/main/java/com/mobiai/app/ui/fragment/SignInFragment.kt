package com.mobiai.app.ui.fragment

import android.util.Patterns
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.mobiai.base.basecode.storage.SharedPreferenceUtils
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
        val auth = FirebaseAuth.getInstance()
        val email = binding.inputEmail.text.toString().trim()
        val password = binding.inputPass.text.toString().trim()

        auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener(requireActivity()) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                replaceFragment(HomeFragment.instance())
                SharedPreferenceUtils.emailLogin = email
            } else {
                Toast.makeText(requireContext(),"Fail!",Toast.LENGTH_SHORT).show()
            }
        }
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