package com.mobiai.app.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.mobiai.R
import com.mobiai.app.model.Rank
import com.mobiai.app.model.User
import com.mobiai.app.utils.gone
import com.mobiai.app.utils.visible
import com.mobiai.base.basecode.adapter.BaseAdapter
import com.mobiai.databinding.ItemRankBinding

class RankAdapter(private val context: Context) : BaseAdapter<User,ItemRankBinding>() {
    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup): ItemRankBinding {
        return ItemRankBinding.inflate(inflater,parent,false)
    }
    private lateinit var launcher: ActivityResultLauncher<Intent>

    override fun bind(binding: ItemRankBinding, item: User, position: Int) {
        binding.name.text = item.name
        binding.txtExperience.text = item.totalXp.toString()
        Glide.with(context).load(item.urlImage).into(binding.img)
        when (position){
            0 -> {
                binding.txtRank.gone()
                binding.imgRank.visible()
                Glide.with(context).load(R.drawable.rank_one).into(binding.imgRank)
            }
            1 -> {
                binding.txtRank.gone()
                binding.imgRank.visible()
                Glide.with(context).load(R.drawable.rank_two).into(binding.imgRank)
            }
            2 -> {
                binding.txtRank.gone()
                binding.imgRank.visible()
                Glide.with(context).load(R.drawable.rank_three).into(binding.imgRank)
            }
            else -> {
                binding.txtRank.visible()
                binding.imgRank.gone()
                binding.txtRank.text = "${(position +1)}"
            }
        }
    }
}