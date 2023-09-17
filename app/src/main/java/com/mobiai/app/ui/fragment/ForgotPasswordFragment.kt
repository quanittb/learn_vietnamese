package com.mobiai.app.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import com.mobiai.base.basecode.ui.fragment.BaseFragment
import com.mobiai.databinding.ForgotPasswordFragmentBinding
import com.mobiai.databinding.SignUpFragmentBinding

class ForgotPasswordFragment : BaseFragment<ForgotPasswordFragmentBinding>() {

    companion object{
        fun instance() : ForgotPasswordFragment{
            return newInstance(ForgotPasswordFragment::class.java)
        }
    }
    override fun initView() {
        binding.icBack.setOnClickListener {
            handlerBackPressed()
        }

    }

    override fun handlerBackPressed() {
        super.handlerBackPressed()
        closeFragment(this)
    }
    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ForgotPasswordFragmentBinding {
        return ForgotPasswordFragmentBinding.inflate(inflater, container,false)
    }
}