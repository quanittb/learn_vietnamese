package com.mobiai.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mobiai.app.model.Lesson
import com.mobiai.base.basecode.adapter.BaseAdapter
import com.mobiai.databinding.ItemStudyBinding

class LessonAdapter(val context : Context, val listener : OnLessonClickListener) : BaseAdapter<Lesson, ItemStudyBinding>() {

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup): ItemStudyBinding {
        return ItemStudyBinding.inflate(inflater, parent, false)
    }

    override fun bind(binding: ItemStudyBinding, item: Lesson, position: Int) {
        binding.txtTitle.text = item.title
        Glide.with(context).load(item.image).diskCacheStrategy(DiskCacheStrategy.ALL)
            .skipMemoryCache(true).into(binding.image)
        binding.sbPercent.setProgress(item.numberDone.toFloat())

        binding.root.setOnClickListener {
            listener.onClickItemListener(item)
            notifyItemChanged(position)
        }
    }


    interface OnLessonClickListener {
        fun onClickItemListener(lesson: Lesson?)
    }
}