package com.mobiai.app.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import com.mobiai.base.basecode.ui.fragment.BaseFragment
import com.mobiai.databinding.FragmentQuestionBinding

class QuestionFragment : BaseFragment<FragmentQuestionBinding>() {
    companion object{
        fun instance(): QuestionFragment{
            return newInstance(QuestionFragment::class.java)
        }
    }

    override fun initView() {

        binding.dvDotView.init(6,1)
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentQuestionBinding {
        return FragmentQuestionBinding.inflate(inflater,container,false)
    }
}