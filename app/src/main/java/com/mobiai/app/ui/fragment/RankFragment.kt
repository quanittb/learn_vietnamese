package com.mobiai.app.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.mobiai.app.adapter.GiftAdapter
import com.mobiai.app.adapter.ItemSpacingDecoration
import com.mobiai.app.adapter.RankAdapter
import com.mobiai.app.ui.dataclass.Gift
import com.mobiai.app.ui.dataclass.Rank
import com.mobiai.base.basecode.ui.fragment.BaseFragment
import com.mobiai.databinding.FragmentRankBinding

class RankFragment : BaseFragment<FragmentRankBinding>() {
   companion object{
       fun instance() : RankFragment{
           return newInstance(RankFragment::class.java)
       }
   }
    private var  rankAdapter : RankAdapter? = null

    override fun initView() {
        initData()
    }
    fun initData() {
        rankAdapter = RankAdapter(requireContext())
        var listRank : ArrayList<Rank> = arrayListOf()
        listRank.add(Rank("","ABCD",1000))
        listRank.add(Rank("","ABCDE",5000))
        listRank.add(Rank("","ABCDF",6000))
        listRank.add(Rank("","ABCDG",4000))
        listRank.add(Rank("","ABCDH",3000))
        listRank.add(Rank("","ABCDi",2000))
        binding.recyclerViewRank.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        binding.recyclerViewRank.addItemDecoration(ItemSpacingDecoration(7,8))
        rankAdapter?.setItems(listRank)
        binding.recyclerViewRank.adapter = rankAdapter
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentRankBinding {
        return FragmentRankBinding.inflate(inflater,container,false)
    }
}