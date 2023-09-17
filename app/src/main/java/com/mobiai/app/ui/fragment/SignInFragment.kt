package com.mobiai.app.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.mobiai.base.basecode.storage.SharedPreferenceUtils
import com.mobiai.base.basecode.ui.fragment.BaseFragment
import com.mobiai.databinding.LoginFragmentBinding
import com.mobiai.databinding.SignUpFragmentBinding

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
            SignIn()
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
    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): LoginFragmentBinding {
        return LoginFragmentBinding.inflate(inflater, container,false)
    }
}