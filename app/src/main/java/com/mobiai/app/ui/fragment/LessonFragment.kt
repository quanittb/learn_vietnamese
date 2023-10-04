package com.mobiai.app.ui.fragment

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mobiai.app.adapter.LanguageAdapter
import com.mobiai.app.adapter.LessonAdapter
import com.mobiai.app.model.Lesson
import com.mobiai.base.basecode.language.Language
import com.mobiai.base.basecode.ui.fragment.BaseFragment
import com.mobiai.databinding.FragmentHomeBinding
import com.mobiai.databinding.FragmentLessonBinding

class LessonFragment : BaseFragment<FragmentLessonBinding>() {

    companion object{
        fun instance() : LessonFragment{
            return newInstance(LessonFragment::class.java)
        }
    }

    override fun initView() {

    }

    override fun handlerBackPressed() {
        super.handlerBackPressed()
        closeFragment(this)
    }



    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentLessonBinding {
        return FragmentLessonBinding.inflate(inflater, container,false)
    }
}