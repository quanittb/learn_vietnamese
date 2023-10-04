package com.mobiai.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mobiai.app.ui.dataclass.Rank
import com.mobiai.base.basecode.adapter.BaseAdapter
import com.mobiai.databinding.ItemRankBinding

class RankAdapter(private val context: Context) : BaseAdapter<Rank,ItemRankBinding>() {
    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup): ItemRankBinding {
        return ItemRankBinding.inflate(inflater,parent,false)
    }

    override fun bind(binding: ItemRankBinding, item: Rank, position: Int) {
        binding.name.text = item.name
        binding.txtExperience.text = item.experience.toString()
    }
}