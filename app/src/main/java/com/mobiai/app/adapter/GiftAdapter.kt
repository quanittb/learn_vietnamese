package com.mobiai.app.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mobiai.R
import com.mobiai.app.App
import com.mobiai.app.model.Gift
import com.mobiai.app.model.User
import com.mobiai.app.ui.dialog.SuccessGiftDialog
import com.mobiai.app.utils.Announcer
import com.mobiai.app.utils.gone
import com.mobiai.app.utils.visible
import com.mobiai.base.basecode.adapter.BaseAdapter
import com.mobiai.base.basecode.storage.SharedPreferenceUtils
import com.mobiai.databinding.ItemGiftBinding

class GiftAdapter(val context: Context) : BaseAdapter<Gift,ItemGiftBinding>() {
    private var successGiftDialog: SuccessGiftDialog? = null

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
        Glide.with(context).load(item.imageUrl).into(binding.image)

        binding.btnReceive.setOnClickListener {
            successGiftDialog = SuccessGiftDialog(context)
            successGiftDialog!!.show()
        }
        getRubyCurrent(item,binding)
    }
    fun getRubyCurrent(item: Gift,binding: ItemGiftBinding){
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference(App.USER)
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (userSnapshot in dataSnapshot.children) {
                    val user = userSnapshot.getValue(User::class.java)
                    if (user != null) {
                        if (user.email == SharedPreferenceUtils.emailLogin) {
                            if(user.ruby < item.ruby){
                                binding.btnReceive.background = ContextCompat.getDrawable(context, R.drawable.bg_disable_receive_gift)
                                binding.btnReceive.setOnClickListener(null)
                            }
                            else
                                binding.btnReceive.background = ContextCompat.getDrawable(context, R.drawable.bg_enable_receive_gift)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException())
            }
        })
    }
}