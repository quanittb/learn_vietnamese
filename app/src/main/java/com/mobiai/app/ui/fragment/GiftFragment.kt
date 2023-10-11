package com.mobiai.app.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobiai.app.adapter.GiftAdapter
import com.mobiai.app.adapter.ItemSpacingDecoration
import com.mobiai.app.model.Gift
import com.mobiai.base.basecode.ui.fragment.BaseFragment
import com.mobiai.databinding.FragmentGiftBinding

class GiftFragment : BaseFragment<FragmentGiftBinding>() {
    companion object{
        fun instance(): GiftFragment{
            return newInstance(GiftFragment::class.java)
        }
    }
    private var  giftAdapter : GiftAdapter? = null

    override fun initView() {
        initData()
    }
    fun initData() {
        giftAdapter = GiftAdapter(requireContext())
        var listGift : ArrayList<Gift> = arrayListOf()
        listGift.add(Gift("Bộ sách nói Thời thơ ấu ","nan","Làm quen với các Tổng thống Hợp Chủng Quốc Hoa Kỳ và các nguyên",1900))
        listGift.add(Gift("Bộ sách nói Thời thơ ấu ","nan","Làm quen với các Tổng thống Hợp Chủng Quốc Hoa Kỳ và các nguyên",1800))
        listGift.add(Gift("Bộ sách nói Thời thơ ấu ","nan","Làm quen với các Tổng thống Hợp Chủng Quốc Hoa Kỳ và các nguyên",1700))
        listGift.add(Gift("Bộ sách nói Thời thơ ấu ","nan","Làm quen với các Tổng thống Hợp Chủng Quốc Hoa Kỳ và các nguyên",1600))
        listGift.add(Gift("Bộ sách nói Thời thơ ấu ","nan","Làm quen với các Tổng thống Hợp Chủng Quốc Hoa Kỳ và các nguyên",1500))
        listGift.add(Gift("Bộ sách nói Thời thơ ấu ","nan","Làm quen với các Tổng thống Hợp Chủng Quốc Hoa Kỳ và các nguyên",2000))
        listGift.add(Gift("Bộ sách nói Thời thơ ấu ","nan","Làm quen với các Tổng thống Hợp Chủng Quốc Hoa Kỳ và các nguyên",2100))
        listGift.add(Gift("Bộ sách nói Thời thơ ấu ","nan","Làm quen với các Tổng thống Hợp Chủng Quốc Hoa Kỳ và các nguyên,Làm quen với các Tổng thống Hợp Chủng Quốc Hoa Kỳ và các nguyên",2200))
        listGift.add(Gift("Bộ sách nói Thời thơ ấu ","nan","Làm quen với các Tổng thống Hợp Chủng Quốc Hoa Kỳ và các nguyên,Làm quen với các Tổng thống Hợp Chủng Quốc Hoa Kỳ và các nguyên",2200))
        listGift.add(Gift("Bộ sách nói Thời thơ ấu ","nan","Làm quen với các Tổng thống Hợp Chủng Quốc Hoa Kỳ và các nguyên,Làm quen với các Tổng thống Hợp Chủng Quốc Hoa Kỳ và các nguyên",2200))
        listGift.add(Gift("Bộ sách nói Thời thơ ấu ","nan","Làm quen với các Tổng thống Hợp Chủng Quốc Hoa Kỳ và các nguyên,Làm quen với các Tổng thống Hợp Chủng Quốc Hoa Kỳ và các nguyên",2200))


        giftAdapter?.setItems(listGift)
        binding.recyclerViewGift.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        binding.recyclerViewGift.addItemDecoration(ItemSpacingDecoration(7,8))
        binding.recyclerViewGift.adapter = giftAdapter
    }
    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentGiftBinding {
        return FragmentGiftBinding.inflate(inflater,container,false)
    }
}