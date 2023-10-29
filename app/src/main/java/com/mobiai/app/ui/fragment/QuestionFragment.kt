package com.mobiai.app.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mobiai.app.App
import com.mobiai.app.adapter.ClickNext
import com.mobiai.app.adapter.QuestionAdapter
import com.mobiai.app.model.Question
import com.mobiai.app.utils.makeGone
import com.mobiai.app.utils.makeVisible
import com.mobiai.base.basecode.extensions.GetListDataFromFirebase
import com.mobiai.base.basecode.extensions.showToast
import com.mobiai.base.basecode.ui.fragment.BaseFragment
import com.mobiai.databinding.FragmentQuestionBinding
import kotlin.random.Random

class QuestionFragment : BaseFragment<FragmentQuestionBinding>() {
    companion object{
        val NUMBER_TOPIC = "NUMBER_TOPIC"
        val NUMBER_LESSON = "NUMBER_LESSON"
        fun instance(numberTopic: String, numberLesson: Int): QuestionFragment{
            Bundle().apply {
                putString(NUMBER_TOPIC,numberTopic)
                putInt(NUMBER_LESSON,numberLesson)
            return newInstance(QuestionFragment::class.java,this)
            }
        }
    }
    val MAX_SIZE = 10
    private var numberTopic:String? = null
    private var numberLesson:Int = 0
    private var listDataWithTopic = arrayListOf<Question>()
    val level1List = mutableListOf<Question>()
    val level2List = mutableListOf<Question>()
    val level3List = mutableListOf<Question>()
    val level4List = mutableListOf<Question>()
    val level5List = mutableListOf<Question>()
    var questionAdapter : QuestionAdapter? = null
    var listQuestion : ArrayList<Question>? = null
    override fun initView() {
        arguments.let {
            if (it != null) {
                numberTopic = it.getString(NUMBER_TOPIC)
                numberLesson = it.getInt(NUMBER_LESSON)
            }
        }
        getData(object : GetListDataFromFirebase<Question>{
            override fun getListDataSuccess(list: List<Question>) {
                for(i in list){
                    when (i.level){
                        LessonFragment.LEVEL_1 -> level1List.add(i)
                        LessonFragment.LEVEL_2 -> level2List.add(i)
                        LessonFragment.LEVEL_3 -> level3List.add(i)
                        LessonFragment.LEVEL_4 -> level4List.add(i)
                        LessonFragment.LEVEL_5 -> level5List.add(i)
                    }
                }
                listQuestion = arrayListOf()
                val random = Random(100)
                listQuestion?.addAll(level1List.shuffled(random).take(5))
                listQuestion?.addAll(level2List.shuffled(random).take(3))
                listQuestion?.addAll(level3List.shuffled(random).take(2))
                questionAdapter = QuestionAdapter(requireContext(),object : ClickNext{
                    override fun clickNext(position: Int) {
                        binding.dvDotView.init(MAX_SIZE,position)
                        binding.rcvQuestions.scrollToPosition(position)
                    }
                })
                questionAdapter?.setItems(listQuestion!!)
                binding.rcvQuestions.isNestedScrollingEnabled = false
                binding.rcvQuestions.adapter = questionAdapter
            }

            override fun getDataFail(err: String) {
                requireContext().showToast(err)
            }

        })
        binding.dvDotView.init(MAX_SIZE,1)

    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentQuestionBinding {
        return FragmentQuestionBinding.inflate(inflater,container,false)
    }

    private fun getData(getListDataFromFirebase: GetListDataFromFirebase<Question>){
        binding.frLoading.makeVisible()
        val db = FirebaseDatabase.getInstance()
        val ref = db.getReference(App.QUESTION)
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (userSnapshot in dataSnapshot.children) {
                    // Lấy dữ liệu từ mỗi child node và chuyển đổi thành đối tượng User
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
}