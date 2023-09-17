package com.mobiai.app.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
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
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): LoginFragmentBinding {
        return LoginFragmentBinding.inflate(inflater, container,false)
    }
}