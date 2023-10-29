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
import com.mobiai.app.model.Question
import com.mobiai.app.utils.makeGone
import com.mobiai.app.utils.makeVisible
import com.mobiai.app.utils.setOnSafeClickListener
import com.mobiai.base.basecode.extensions.GetListDataFromFirebase
import com.mobiai.base.basecode.extensions.showToast
import com.mobiai.base.basecode.ui.fragment.BaseFragment
import com.mobiai.databinding.FragmentLessonBinding
import kotlin.random.Random

class LessonFragment : BaseFragment<FragmentLessonBinding>() {

    companion object{
        val NUMBER_TOPIC = "NUMBER_TOPIC"
        const val LEVEL_1 = 1;
        const val LEVEL_2 = 2;
        const val LEVEL_3 = 3;
        const val LEVEL_4 = 4;
        const val LEVEL_5 = 5;
        fun instance(numberTopic: String) : LessonFragment{
            Bundle().apply {
                putString(NUMBER_TOPIC, numberTopic)
                return newInstance(LessonFragment::class.java, this)
            }
        }
    }

    private var numberTopic:String? = null
    private var listDataWithTopic = arrayListOf<Question>()
    val level1List = mutableListOf<Question>()
    val level2List = mutableListOf<Question>()
    val level3List = mutableListOf<Question>()
    val level4List = mutableListOf<Question>()
    val level5List = mutableListOf<Question>()
    override fun initView() {
        arguments.let {
            if (it != null) {
                numberTopic = it.getString(NUMBER_TOPIC)
                Log.d("TAG", "initView: $numberTopic")
            }
        }
        getData(object : GetListDataFromFirebase<Question>{
            override fun getListDataSuccess(list: List<Question>) {
                for(i in list){
                    when (i.level){
                        LEVEL_1 -> level1List.add(i)
                        LEVEL_2 -> level2List.add(i)
                        LEVEL_3 -> level3List.add(i)
                        LEVEL_4 -> level4List.add(i)
                        LEVEL_5 -> level5List.add(i)
                    }
                }
            }

            override fun getDataFail(err: String) {
                requireContext().showToast(err)
            }

        })

        binding.ivBack.setOnSafeClickListener(300) {
            handlerBackPressed()
        }

        binding.ivStart.setOnClickListener {
            val listQuestion : ArrayList<Question> = arrayListOf()
            val random = Random(100)

            listQuestion.addAll(level1List.shuffled(random).take(5))
            listQuestion.addAll(level2List.shuffled(random).take(3))
            listQuestion.addAll(level3List.shuffled(random).take(2))
            addFragment(QuestionFragment.instance(numberTopic!!,1))
        }
    }

    override fun handlerBackPressed() {
        super.handlerBackPressed()
        closeFragment(this)
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


    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentLessonBinding {
        return FragmentLessonBinding.inflate(inflater, container,false)
    }
}