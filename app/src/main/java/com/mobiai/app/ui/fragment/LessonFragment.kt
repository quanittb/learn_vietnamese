package com.mobiai.app.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mobiai.app.App
import com.mobiai.app.model.Question
import com.mobiai.app.model.User
import com.mobiai.app.utils.makeGone
import com.mobiai.app.utils.makeVisible
import com.mobiai.app.utils.setOnSafeClickListener
import com.mobiai.base.basecode.storage.SharedPreferenceUtils
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
    private var listDataWithTopic = arrayListOf<Question>()
    override fun initView() {
        arguments.let {
            if (it != null) {
                numberTopic = it.getString(NUMBER_TOPIC)
                Log.d("TAG", "initView: $numberTopic")
            }
        }

        //getData()

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


    /*private fun getData(){
        val db = FirebaseDatabase.getInstance()
        val ref = db.getReference(App.LESSON)
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (userSnapshot in dataSnapshot.children) {
                    // Lấy dữ liệu từ mỗi child node và chuyển đổi thành đối tượng User
                    val question = userSnapshot.getValue(Question::class.java)
                    if (question != null && question.topicCode== numberTopic) {
                        val question = Question(question.questionCode,question.content,question.topicCode,question.level,question.option1,question.option2,question.option3,question.option4,question.answer,question.urlImg,question.typeCode)
                        listDataWithTopic.add(question)
                        Log.d("TAG", "onDataChange: $question")

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException())
            }
        })

    }*/


    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentLessonBinding {
        return FragmentLessonBinding.inflate(inflater, container,false)
    }
}