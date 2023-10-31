package com.mobiai.app.adapter

import android.content.Context
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.jaygoo.widget.RangeSeekBar
import com.mobiai.R
import com.mobiai.app.App
import com.mobiai.app.model.Lessons
import com.mobiai.app.model.Question
import com.mobiai.app.model.Topic
import com.mobiai.app.utils.makeGone
import com.mobiai.app.utils.makeVisible
import com.mobiai.base.basecode.adapter.BaseAdapter
import com.mobiai.base.basecode.extensions.GetListDataFromFirebase
import com.mobiai.databinding.ItemStudyBinding

class TopicAdapter(val context : Context, val listener : OnLessonClickListener) : BaseAdapter<Topic, ItemStudyBinding>() {

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup): ItemStudyBinding {
        return ItemStudyBinding.inflate(inflater, parent, false)
    }

    private var listLessonOfTopic = arrayListOf<Lessons>()
    override fun bind(binding: ItemStudyBinding, item: Topic, position: Int) {
        binding.txtTitle.text = "${item.topicCode}: ${item.name}"
        binding.txtNumberLesson.text = item.numberLesson.toString()
        if (item.urlImg.isEmpty()){
            binding.image.setImageDrawable(context.resources.getDrawable(R.drawable.img_part1))
        }
        else{
            Glide.with(context).load(item.urlImg).diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(true).into(binding.image)
        }
        getData(binding.sbPercent,item.topicCode)
       // binding.sbPercent.setProgress(item.numberLesson.toFloat())

        binding.root.setOnClickListener {
            listener.onClickItemListener(item)
            notifyItemChanged(position)
        }
    }

    private fun getData(view: RangeSeekBar, topicCode:String){
        var numberDone = 0f
        val db = FirebaseDatabase.getInstance()
        val ref1 = db.getReference(App.LESSON)
        val ref2 = db.getReference(App.RESULTS)
            //lesson
        ref1.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (userSnapshot in dataSnapshot.children) {
                    val question = userSnapshot.getValue(Lessons::class.java)
                    if (question != null) {
                        if (question.topicCode == topicCode){
                            listLessonOfTopic.add(question)
                        }
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("TAG", "Failed to read value.", error.toException())
            }
        })

        // results
        ref2.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (userSnapshot in dataSnapshot.children) {
                    val results = userSnapshot.getValue(Results::class.java)
                    if (results != null) {

                        for (item in listLessonOfTopic){
                            if (item.lessonCode == results){
                                numberDone++
                            }
                        }
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("TAG", "Failed to read value.", error.toException())
            }
        })

        view.setProgress(numberDone)

    }

    interface OnLessonClickListener {
        fun onClickItemListener(lesson: Topic?)
    }
}