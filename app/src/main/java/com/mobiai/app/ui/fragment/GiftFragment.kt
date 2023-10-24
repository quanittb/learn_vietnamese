package com.mobiai.app.ui.fragment

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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
        const val IMAGEURL = "imageurl"
        const val DESCRIPTION = "description"
        const val TITLE = "title"
        const val RUBY  = "ruby"
    }
    private var  giftAdapter : GiftAdapter? = null
    var listGift : ArrayList<Gift>? = null

    override fun initView() {

        initData()

    }
    fun initData() {
        listGift = arrayListOf()
        giftAdapter = GiftAdapter(requireContext())
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("gift")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()){
                    for(i in dataSnapshot.children){
                        var gift = Gift()
                        gift.imageUrl = i.child("$IMAGEURL").value.toString()
                        gift.description = i.child("$DESCRIPTION").value.toString()
                        gift.title = i.child("$TITLE").value.toString()
                        gift.ruby = i.child("$RUBY").value.toString().toInt()
                        listGift?.add(gift)
                    }
                    giftAdapter?.setItems(listGift!!)
                    binding.recyclerViewGift.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
                    binding.recyclerViewGift.addItemDecoration(ItemSpacingDecoration(7,8))
                    binding.recyclerViewGift.adapter = giftAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Xử lý lỗi nếu có
            }
        })

        Log.d("abcd","list : ${listGift?.size}")

    }
    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentGiftBinding {
        return FragmentGiftBinding.inflate(inflater,container,false)
    }
}