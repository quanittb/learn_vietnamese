package com.mobiai.app.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import com.mobiai.base.basecode.ui.fragment.BaseFragment
import com.mobiai.databinding.FragmentGiftBinding

class GiftFragment : BaseFragment<FragmentGiftBinding>() {
    companion object{
        fun instance(): GiftFragment{
            return newInstance(GiftFragment::class.java)
        }
    }
    override fun initView() {

    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentGiftBinding {
        return FragmentGiftBinding.inflate(inflater,container,false)
    }
}