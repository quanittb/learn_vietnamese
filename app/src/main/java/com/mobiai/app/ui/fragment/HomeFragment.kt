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

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    companion object{
        fun instance() : HomeFragment{
            return newInstance(HomeFragment::class.java)
        }
    }
    lateinit var lessonAdapter: LessonAdapter
    var listLesson: ArrayList<Lesson> = arrayListOf()

    override fun initView() {
        initAdapter()
        //addFragment(PronunciationFragment.instance())
    }

    private fun initAdapter(){
        initData()
        lessonAdapter =  LessonAdapter(requireContext(), object : LessonAdapter.OnLessonClickListener{
            override fun onClickItemListener(lesson: Lesson?) {
            }
        })
        lessonAdapter.setItems(listLesson)
        binding.rcvLesson.adapter = lessonAdapter
    }
    private fun initData(){
        listLesson.addAll(Lesson.lessonList(requireContext()))
        Log.d("TAG", "initData: ${listLesson.size}")
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container,false)
    }
}