package com.mobiai.app.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import com.mobiai.app.utils.setOnSafeClickListener
import com.mobiai.base.basecode.ui.fragment.BaseFragment
import com.mobiai.databinding.FragmentLessonBinding

class LessonFragment : BaseFragment<FragmentLessonBinding>() {

    companion object{
        fun instance() : LessonFragment{
            return newInstance(LessonFragment::class.java)
        }
    }

    override fun initView() {
        binding.ivBack.setOnSafeClickListener(300) {
            handlerBackPressed()
        }

        binding.ivStart.setOnClickListener {
            addFragment(QuestionFragment.instance())
        }
    }

    override fun handlerBackPressed() {
        super.handlerBackPressed()
        closeFragment(this)
    }



    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentLessonBinding {
        return FragmentLessonBinding.inflate(inflater, container,false)
    }
}