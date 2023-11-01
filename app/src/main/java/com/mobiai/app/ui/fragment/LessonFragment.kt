package com.mobiai.app.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.jaygoo.widget.RangeSeekBar
import com.mobiai.R
import com.mobiai.app.App
import com.mobiai.app.model.Lessons
import com.mobiai.app.model.Question
import com.mobiai.app.model.Results
import com.mobiai.app.utils.makeGone
import com.mobiai.app.utils.makeVisible
import com.mobiai.app.utils.setOnSafeClickListener
import com.mobiai.base.basecode.extensions.GetListDataFromFirebase
import com.mobiai.base.basecode.extensions.showToast
import com.mobiai.base.basecode.storage.SharedPreferenceUtils
import com.mobiai.base.basecode.ui.fragment.BaseFragment
import com.mobiai.databinding.FragmentLessonBinding
import java.util.Random

class LessonFragment : BaseFragment<FragmentLessonBinding>() {

    companion object{
        val NUMBER_TOPIC = "NUMBER_TOPIC"
        const val LEVEL_1 = 1;
        const val LEVEL_2 = 2;
        const val LEVEL_3 = 3;
        const val LEVEL_4 = 4;
        const val LEVEL_5 = 5;
        const val LEVEL_6 = 5;
        fun instance(numberTopic: String) : LessonFragment{
            Bundle().apply {
                putString(NUMBER_TOPIC, numberTopic)
                return newInstance(LessonFragment::class.java, this)
            }
        }
    }

    private var numberTopic:String? = null
    private var listDataWithTopic = arrayListOf<Question>()
    private var listDataLessonWithTopic = arrayListOf<Lessons>()
    private val level1List = mutableListOf<Question>()
    private val level2List = mutableListOf<Question>()
    private val level3List = mutableListOf<Question>()
    private val level4List = mutableListOf<Question>()
    private val level5List = mutableListOf<Question>()
    private val level6List = mutableListOf<Question>()


    override fun initView() {
        arguments.let {
            if (it != null) {
                numberTopic = it.getString(NUMBER_TOPIC)
                Log.d("TAG", "initView: $numberTopic")
            }
        }
        disableAllItem()
        getDataLesson()
        getData(object : GetListDataFromFirebase<Question>{
            override fun getListDataSuccess(list: List<Question>) {
                for(i in list){
                    when (i.level){
                        LEVEL_1 -> level1List.add(i)
                        LEVEL_2 -> level2List.add(i)
                        LEVEL_3 -> level3List.add(i)
                        LEVEL_4 -> level4List.add(i)
                        LEVEL_5 -> level5List.add(i)
                        LEVEL_6 -> level6List.add(i)
                    }
                }
            }

            override fun getDataFail(err: String) {
                requireContext().showToast(err)
            }

        })
        showItemClick()

        binding.ivBack.setOnSafeClickListener(300) {
            handlerBackPressed()
        }

        binding.frStudy1.setOnSafeClickListener {
            //addFragmentWithLevel(LEVEL_1)
            addFragment(QuestionFragment.instance(getListRandomForLevel(getNumberLevel(listDataLessonWithTopic[0].lessonCode)),listDataLessonWithTopic[0].lessonCode))
        }

        binding.frStudy2.setOnSafeClickListener {
           // addFragmentWithLevel(LEVEL_2)
            addFragment(QuestionFragment.instance(getListRandomForLevel(getNumberLevel(listDataLessonWithTopic[1].lessonCode)),listDataLessonWithTopic[1].lessonCode))

        }

        binding.frStudy3.setOnSafeClickListener {
            //addFragmentWithLevel(LEVEL_3)
            addFragment(QuestionFragment.instance(getListRandomForLevel(getNumberLevel(listDataLessonWithTopic[2].lessonCode)),listDataLessonWithTopic[2].lessonCode))

        }

        binding.frStudy4.setOnSafeClickListener {
            //addFragmentWithLevel(LEVEL_4)
            addFragment(QuestionFragment.instance(getListRandomForLevel(getNumberLevel(listDataLessonWithTopic[3].lessonCode)),listDataLessonWithTopic[3].lessonCode))

        }


        binding.frStudy5.setOnSafeClickListener {
           // addFragmentWithLevel(LEVEL_5)
            addFragment(QuestionFragment.instance(getListRandomForLevel(getNumberLevel(listDataLessonWithTopic[4].lessonCode)),listDataLessonWithTopic[4].lessonCode))

        }

        binding.frStudy6.setOnSafeClickListener {
          //  addFragmentWithLevel(LEVEL_6)
            addFragment(QuestionFragment.instance(getListRandomForLevel(getNumberLevel(listDataLessonWithTopic[4].lessonCode)),listDataLessonWithTopic[4].lessonCode))

        }

    }

    private fun disableAllItem(){
        binding.frStudy2.isEnabled = false
        binding.frStudy3.isEnabled = false
        binding.frStudy4.isEnabled = false
        binding.frStudy5.isEnabled = false
        binding.frStudy6.isEnabled = false
    }
    private fun showItemClick(){
        val db = FirebaseDatabase.getInstance()
        val ref2 = db.getReference(App.RESULTS)
        // results
        ref2.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (userSnapshot in dataSnapshot.children) {
                    val results = userSnapshot.getValue(Results::class.java)
                    if (results != null) {
                        if (results.numberCorrect > 8){
                            if (results.userName == SharedPreferenceUtils.emailLogin){
                                for (item in listDataLessonWithTopic){
                                    if (results.lessonCode == item.lessonCode){
                                        enableWithItem(getNumberLevel(item.lessonCode)  + 1)
                                        Log.d("TAG", "onDataChange: ${item.lessonCode}")

                                    }
                                }
                            }
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("TAG", "Failed to read value.", error.toException())
            }
        })
    }

    private fun enableWithItem(lessonCode:Int){
        when (lessonCode) {
            getNumberLevel(listDataLessonWithTopic[0].lessonCode) -> {
                binding.frStudy1.isEnabled = true
            }
            getNumberLevel(listDataLessonWithTopic[1].lessonCode) -> {
                binding.frStudy2.isEnabled = true
                binding.ivStudy2.setImageDrawable(requireContext().resources.getDrawable(R.drawable.ic_study_5_on))
                binding.bgLessonNext2.makeVisible()
            }
            getNumberLevel(listDataLessonWithTopic[2].lessonCode) -> {
                binding.frStudy2.isEnabled = true
                binding.frStudy3.isEnabled = true
                binding.ivStudy3.setImageDrawable(requireContext().resources.getDrawable(R.drawable.ic_study_3_on))
                binding.bgLessonNext3.makeVisible()
            }
            getNumberLevel(listDataLessonWithTopic[3].lessonCode) -> {
                binding.frStudy2.isEnabled = true
                binding.frStudy3.isEnabled = true
                binding.frStudy4.isEnabled = true
                binding.ivStudy4.setImageDrawable(requireContext().resources.getDrawable(R.drawable.ic_study_4_on))
                binding.bgLessonNext4.makeVisible()
            }
            getNumberLevel(listDataLessonWithTopic[4].lessonCode) -> {
                binding.frStudy2.isEnabled = true
                binding.frStudy3.isEnabled = true
                binding.frStudy4.isEnabled = true
                binding.frStudy5.isEnabled = true
                binding.ivStudy5.setImageDrawable(requireContext().resources.getDrawable(R.drawable.ic_study_5_on))
                binding.bgLessonNext5.makeVisible()
            }
            getNumberLevel(listDataLessonWithTopic[4].lessonCode) -> {
                binding.frStudy2.isEnabled = true
                binding.frStudy3.isEnabled = true
                binding.frStudy4.isEnabled = true
                binding.frStudy5.isEnabled = true
                binding.frStudy6.isEnabled = true
                binding.ivStudy6.setImageDrawable(requireContext().resources.getDrawable(R.drawable.ic_study_6_on))
                binding.bgLessonNext6.makeVisible()
            }
        }
    }


    private fun addFragmentWithLevel(level: Int){
        for (item in listDataLessonWithTopic){
            if (getNumberLevel(item.lessonCode) == level){
                addFragment(QuestionFragment.instance(getListRandomForLevel(getNumberLevel(item.lessonCode)),item.lessonCode))
                break
            }
        }
    }
    private fun getNumberLevel(inputString:String):Int {
        // Sử dụng regex để lấy ra số từ chuỗi
        val regex = Regex("\\d+")
        val matchResult = regex.find(inputString)

        // Kiểm tra xem có kết quả phù hợp nào không
        if (matchResult != null) {
            return matchResult.value.toInt()
        } else {
            return -1
        }
    }

    override fun handlerBackPressed() {
        super.handlerBackPressed()
        closeFragment(this)
    }

    private fun getDataLesson(){
        binding.frLoading.makeVisible()
        val db = FirebaseDatabase.getInstance()
        val ref1 = db.getReference(App.LESSON)
        //lesson
        ref1.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (userSnapshot in dataSnapshot.children) {
                    val lesson = userSnapshot.getValue(Lessons::class.java)
                    if (lesson != null) {
                       if (lesson.topicCode ==  numberTopic){
                           listDataLessonWithTopic.add(lesson)
                       }
                    }
                }
                Log.w("TAG", "listDataLesson: $listDataLessonWithTopic")
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w("TAG", "Failed to read value.", error.toException())
            }
        })
    }



    private fun getData(getListDataFromFirebase: GetListDataFromFirebase<Question>){
        binding.frLoading.makeVisible()
        val db = FirebaseDatabase.getInstance()
        val ref = db.getReference(App.QUESTION)
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (userSnapshot in dataSnapshot.children) {
                    val question = userSnapshot.getValue(Question::class.java)
                    if (question != null && question.topicCode == numberTopic) {
                        listDataWithTopic.add(question)
                    }
                }
                Handler().postDelayed({
                    binding.frLoading.makeGone()
                },1000)
                getListDataFromFirebase.getListDataSuccess(listDataWithTopic)
            }
            override fun onCancelled(error: DatabaseError) {
                getListDataFromFirebase.getDataFail(error.message)
                Handler().postDelayed({
                    binding.frLoading.makeGone()
                },1000)
                Log.w("TAG", "Failed to read value.", error.toException())
            }
        })
    }

    private fun getListRandomForLevel(level : Int): String{
        val listQuestion : ArrayList<Question> = arrayListOf()
        val random = Random()
       var num1 = 0
       var num2 = 0
       var num3 = 0
       var num4 = 0
       var num5 = 0
        when (level){
            1 -> {
                num1 = 5
                num2 = 3
                num3 = 2
            }
            2 -> {
                num1 = 3
                num2 = 5
                num3 = 1
                num4 = 1
            }
            3 -> {
                num1 = 2
                num2 = 5
                num3 = 2
                num4 = 1
            }
            4 -> {
                num2 = 1
                num3 = 1
                num4 = 5
                num5 = 3
            }
            else -> {
                num2 = 1
                num3 = 1
                num4 = 3
                num5 = 5
            }
        }
        listQuestion.addAll(level1List.shuffled(random).take(num1))
        listQuestion.addAll(level2List.shuffled(random).take(num2))
        listQuestion.addAll(level3List.shuffled(random).take(num3))
        listQuestion.addAll(level4List.shuffled(random).take(num4))
        listQuestion.addAll(level5List.shuffled(random).take(num5))
        listQuestion.addAll(level6List.shuffled(random).take(num5))
        val gson = Gson()
        return  gson.toJson(listQuestion)
    }
    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentLessonBinding {
        return FragmentLessonBinding.inflate(inflater, container,false)
    }
}