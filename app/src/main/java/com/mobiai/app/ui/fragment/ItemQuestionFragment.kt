package com.mobiai.app.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.mobiai.R
import com.mobiai.app.App
import com.mobiai.app.adapter.QuestionAdapter
import com.mobiai.app.model.AnsweredQuestions
import com.mobiai.app.model.Question
import com.mobiai.app.model.User
import com.mobiai.app.utils.gone
import com.mobiai.app.utils.visible
import com.mobiai.base.basecode.extensions.LogD
import com.mobiai.base.basecode.storage.SharedPreferenceUtils
import com.mobiai.base.basecode.ui.fragment.BaseFragment
import com.mobiai.databinding.ItemQuestionBinding

class ItemQuestionFragment : BaseFragment<ItemQuestionBinding>() {
    companion object{
        const val TYPE_1 = 1 // type ko có hình ảnh
        const val TYPE_2 = 2 // type hình ảnh
        const val QUESTION ="QUESTION"
        fun instance(question : String) : ItemQuestionFragment{
            Bundle().apply {
                putString(QUESTION,question)
                return newInstance(ItemQuestionFragment::class.java, this)
            }
        }
    }
    var myQuestion : Question? = null
    var clickNext : ClickButtonNext? = null
    var isTrue = false
    override fun initView() {
        arguments.let {
            if(it != null){
                val jsonString = arguments?.getString(QUESTION)
                val gson = Gson()
                myQuestion = gson.fromJson(jsonString, Question::class.java)
            }
            if(myQuestion?.typeCode== TYPE_1){
                binding.ivImageQuestion.gone()
            } else {
                Glide.with(requireContext()).load(myQuestion?.urlImg).into(binding.ivImageQuestion)
                binding.ivImageQuestion.visible()
            }
            announcer?.initTTS(requireContext())
            binding.tvNameQuestion.text = myQuestion?.content
            binding.txtOption1.text = myQuestion?.option1
            binding.txtOption2.text = myQuestion?.option2
            binding.txtOption3.text = myQuestion?.option3
            binding.txtOption4.text = myQuestion?.option4
            binding.btnNext.setOnClickListener {
                clickNext?.clickNext(isTrue)
            }
            checkAnswer(binding.txtOption1)
            checkAnswer(binding.txtOption2)
            checkAnswer(binding.txtOption3)
            checkAnswer(binding.txtOption4)
        }
    }
    fun checkAnswer(view: TextView){
        view.setOnClickListener{
            binding.lnResult.visible()
            binding.tvResult.text = myQuestion?.answer
            if(view.text == myQuestion!!.answer){
                announcer?.readText(requireContext().getString(R.string.correct))
                binding.lnResult.background = ContextCompat.getDrawable(requireContext(),R.drawable.bg_done_question_success)
                binding.tvSuccess.text = requireContext().getString(R.string.correct)
                view.background = ContextCompat.getDrawable(requireContext(),R.drawable.bg_answer_success)
                binding.tvSuccess.setTextColor(ContextCompat.getColor(requireContext(),R.color.color_correct))
                binding.tvResult.setTextColor(ContextCompat.getColor(requireContext(),R.color.color_correct))
                binding.tvTitleAnswer.setTextColor(ContextCompat.getColor(requireContext(),R.color.color_correct))
                binding.btnNext.background = ContextCompat.getDrawable(requireContext(),R.drawable.bg_next_successs)
                binding.txtOption1.isClickable = false
                binding.txtOption2.isClickable = false
                binding.txtOption3.isClickable = false
                binding.txtOption4.isClickable = false
                isTrue = true
                updateOptionToFirebase(myQuestion!!.questionCode,view.text.toString())
            }
            else{
                announcer?.readText(requireContext().getString(R.string.incorrect))
                binding.lnResult.background = ContextCompat.getDrawable(requireContext(),R.drawable.bg_done_question_faild)
                binding.tvSuccess.text = requireContext().getString(R.string.incorrect)
                view.background = ContextCompat.getDrawable(requireContext(),R.drawable.bg_answer_faild)
                binding.tvSuccess.setTextColor(ContextCompat.getColor(requireContext(),R.color.color_incorrect))
                binding.tvResult.setTextColor(ContextCompat.getColor(requireContext(),R.color.color_incorrect))
                binding.tvTitleAnswer.setTextColor(ContextCompat.getColor(requireContext(),R.color.color_incorrect))
                binding.btnNext.background = ContextCompat.getDrawable(requireContext(),R.drawable.bg_next_fail)
                binding.txtOption1.isClickable = false
                binding.txtOption2.isClickable = false
                binding.txtOption3.isClickable = false
                binding.txtOption4.isClickable = false
                updateOptionToFirebase(myQuestion!!.questionCode,view.text.toString())
                isTrue = false
            }
        }
    }
    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ItemQuestionBinding {
        return ItemQuestionBinding.inflate(inflater,container,false)
    }

    fun updateOptionToFirebase(questionCode: String , option : String){
            val codeResult = "${SharedPreferenceUtils.lesssonCode}_${questionCode}_${SharedPreferenceUtils.emailLogin}"
            val db = FirebaseDatabase.getInstance()
            val ref = db.getReference(App.ANSWEREDQUESTIONS)
            var i = 0
            var isExist = false
        val answerUpdate = AnsweredQuestions(questionCode, codeResult, option)
            ref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    for (userSnapshot in dataSnapshot.children) {
                        val answeredQuestions = userSnapshot.getValue(AnsweredQuestions::class.java)
                        LogD("$answeredQuestions")
                        if (answeredQuestions != null) {
                            if (answeredQuestions.codeQuestion == answerUpdate.codeQuestion && i<1) {
                                userSnapshot.key?.let { ref.child(it).setValue(answerUpdate) }
                                i++
                                isExist = true
                                break
                            }
                        }
                    }
                    if(!isExist){
                        ref.push().setValue(answerUpdate)
                        isExist = true
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                    Log.w("TAG", "Failed to read value.", error.toException())
                }
            })
        }
}
interface ClickButtonNext{
    fun clickNext(isTrue : Boolean = false)
}
