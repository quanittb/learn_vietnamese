package com.mobiai.app.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mobiai.BuildConfig
import com.mobiai.app.adapter.ClickNext
import com.mobiai.app.adapter.QuestionAdapter
import com.mobiai.app.adapter.QuestionPagerAdapter
import com.mobiai.app.model.Question
import com.mobiai.base.basecode.extensions.LogD
import com.mobiai.base.basecode.storage.SharedPreferenceUtils
import com.mobiai.base.basecode.ui.fragment.BaseFragment
import com.mobiai.databinding.FragmentQuestionBinding

class QuestionFragment : BaseFragment<FragmentQuestionBinding>() {
    companion object{
        val LIST_JSON = "LIST_JSON"
        val LESSON = "LESSON"
        const val MAX_SIZE = 10
        fun instance(listJson : String, lesson: String): QuestionFragment{
            Bundle().apply {
                putString(LIST_JSON,listJson)
                putString(LESSON,lesson)
            return newInstance(QuestionFragment::class.java,this)
            }
        }
    }

    var listQuestion : ArrayList<Question>? = null
    var listFragment : ArrayList<BaseFragment<*>> = arrayListOf()
    var postion = 0
    var numberCorrect = 0
    var lesson : String? = null
    override fun initView() {
        arguments.let {
            if (it != null) {
                val jsonString = it.getString(LIST_JSON)
                listQuestion = arrayListOf()
                val gson = Gson()
                listQuestion = gson.fromJson(jsonString, object : TypeToken<ArrayList<Question>>() {}.type)
                lesson = it.getString(LESSON)
                SharedPreferenceUtils.lesssonCode = it.getString(LESSON)
            }
        }
        binding.dvDotView.init(MAX_SIZE,1)
        getData()

    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentQuestionBinding {
        return FragmentQuestionBinding.inflate(inflater,container,false)
    }

    fun getData(){
        val gson = Gson()
        for(i in listQuestion!!){
            val jsonString = gson.toJson(i)
            listFragment?.add(ItemQuestionFragment.instance(jsonString).apply {
                clickNext  = object : ClickButtonNext{
                    override fun clickNext(isTrue : Boolean) {
                        if(isTrue)
                            numberCorrect ++
                        if(postion == listFragment.lastIndex){
                            replaceFragment(ResultFragment.instance(numberCorrect))
                        }
                        else{
                            postion ++
                            binding.viewpager.currentItem = postion
                        }

                    }
                }
            })
        }
        val adapter =QuestionPagerAdapter(requireActivity().supportFragmentManager,listFragment,requireContext())
        binding.viewpager.adapter = adapter
        binding.viewpager.setPagingEnabled(false)
    }

}