package com.mobiai.app.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import com.mobiai.base.basecode.ui.fragment.BaseFragment
import com.mobiai.databinding.SignUpFragmentBinding

class SignUpFragment : BaseFragment<SignUpFragmentBinding>() {

    companion object{
        fun instance() : SignUpFragment{
            return newInstance(SignUpFragment::class.java)
        }
    }
    override fun initView() {
        binding.tvSignIn.setOnClickListener {
            replaceFragment(SignInFragment.instance())
        }
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): SignUpFragmentBinding {
        return SignUpFragmentBinding.inflate(inflater, container,false)
    }
}