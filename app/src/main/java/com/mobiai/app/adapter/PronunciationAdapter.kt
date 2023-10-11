package com.mobiai.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mobiai.app.model.Pronunciation
import com.mobiai.app.utils.Announcer
import com.mobiai.base.basecode.adapter.BaseAdapter
import com.mobiai.databinding.ItemPronunciationBinding

    class PronunciationAdapter(val context: Context) : BaseAdapter<Pronunciation,ItemPronunciationBinding>() {
    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ItemPronunciationBinding {
        return ItemPronunciationBinding.inflate(inflater,parent,false)
    }
    override fun bind(binding: ItemPronunciationBinding, item: Pronunciation, position: Int) {
        val announcer = Announcer(context)
        announcer.initTTS(context)
        binding.title.text = item.title
        binding.description.text = item.content
        binding.root.setOnClickListener {
            announcer.readText("${binding.title.text.substring(1)}      ${binding.description.text}")
        }
    }
}