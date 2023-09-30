package com.mobiai.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mobiai.app.ui.dataclass.Gift
import com.mobiai.app.ui.dataclass.Pronunciation
import com.mobiai.app.utils.Announcer
import com.mobiai.base.basecode.adapter.BaseAdapter
import com.mobiai.databinding.ItemGiftBinding

class GiftAdapter(val context: Context) : BaseAdapter<Gift,ItemGiftBinding>() {
    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ItemGiftBinding {
        return ItemGiftBinding.inflate(inflater,parent,false)
    }
    override fun bind(binding: ItemGiftBinding, item: Gift, position: Int) {
        val announcer = Announcer(context)
        announcer.initTTS(context)
        binding.txtTitle.text = item.title
        binding.txtDescription.text = item.description
        binding.txtRuby.text = item.ruby.toString()
    }
}