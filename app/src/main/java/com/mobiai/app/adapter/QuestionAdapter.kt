package com.mobiai.app.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideContext
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mobiai.R
import com.mobiai.app.App
import com.mobiai.app.model.Gift
import com.mobiai.app.model.Question
import com.mobiai.app.model.User
import com.mobiai.app.ui.dialog.SuccessGiftDialog
import com.mobiai.app.utils.Announcer
import com.mobiai.app.utils.gone
import com.mobiai.app.utils.invisible
import com.mobiai.app.utils.visible
import com.mobiai.base.basecode.adapter.BaseAdapter
import com.mobiai.base.basecode.extensions.GetDataFromFirebase
import com.mobiai.base.basecode.extensions.LogD
import com.mobiai.base.basecode.extensions.showToast
import com.mobiai.base.basecode.storage.SharedPreferenceUtils
import com.mobiai.databinding.ItemGiftBinding
import com.mobiai.databinding.ItemQuestionBinding

class QuestionAdapter(val context: Context, val clickNext: ClickNext) : BaseAdapter<Question,ItemQuestionBinding>() {
companion object{
    const val TYPE_1 = 1 // type ko có hình ảnh
    const val TYPE_2 = 2 // type hình ảnh
}
    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ItemQuestionBinding {
        return ItemQuestionBinding.inflate(inflater,parent,false)
    }

    override fun bind(binding: ItemQuestionBinding, item: Question, position: Int) {
        if(item.typeCode== TYPE_1){
            binding.ivImageQuestion.gone()
        } else {
            Glide.with(context).load(item.urlImg).into(binding.ivImageQuestion)
            binding.ivImageQuestion.visible()
        }

        binding.tvNameQuestion.text = item.content
        binding.txtOption1.text = item.option1
        binding.txtOption2.text = item.option2
        binding.txtOption3.text = item.option3
        binding.txtOption4.text = item.option4

        binding.lnResult.visible()
        binding.btnNext.setOnClickListener {
            clickNext.clickNext(position + 1)
            LogD("đã chạy vào click")
        }
    }
}
interface ClickNext{
    fun clickNext(position: Int)
}