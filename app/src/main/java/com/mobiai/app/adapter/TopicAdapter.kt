package com.mobiai.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mobiai.R
import com.mobiai.app.model.Topic
import com.mobiai.base.basecode.adapter.BaseAdapter
import com.mobiai.databinding.ItemStudyBinding

class TopicAdapter(val context : Context, val listener : OnLessonClickListener) : BaseAdapter<Topic, ItemStudyBinding>() {

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup): ItemStudyBinding {
        return ItemStudyBinding.inflate(inflater, parent, false)
    }

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

       // binding.sbPercent.setProgress(item.numberLesson.toFloat())

        binding.root.setOnClickListener {
            listener.onClickItemListener(item)
            notifyItemChanged(position)
        }
    }


    interface OnLessonClickListener {
        fun onClickItemListener(lesson: Topic?)
    }
}