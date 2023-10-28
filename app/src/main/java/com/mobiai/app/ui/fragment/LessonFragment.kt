package com.mobiai.app.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mobiai.app.utils.setOnSafeClickListener
import com.mobiai.base.basecode.ui.fragment.BaseFragment
import com.mobiai.databinding.FragmentLessonBinding

class LessonFragment : BaseFragment<FragmentLessonBinding>() {

    companion object{
        val NUMBER_TOPIC = "NUMBER_TOPIC"
        fun instance(numberTopic: String) : LessonFragment{
            Bundle().apply {
                putString(NUMBER_TOPIC, numberTopic)
                return newInstance(LessonFragment::class.java, this)
            }
        }
    }

    private var numberTopic:String? = null
    override fun initView() {
        arguments.let {
            if (it != null) {
                numberTopic = it.getString(NUMBER_TOPIC)
                Log.d("TAG", "initView: $numberTopic")
            }
        }
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