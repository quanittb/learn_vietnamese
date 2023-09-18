package com.mobiai.app.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import com.mobiai.base.basecode.ui.fragment.BaseFragment
import com.mobiai.databinding.FragmentReminderBinding

class ReminderFragment : BaseFragment<FragmentReminderBinding>() {
    companion object{
        fun instance(): ReminderFragment{
            return newInstance(ReminderFragment::class.java)
        }
    }
    override fun initView() {

    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentReminderBinding {
        return FragmentReminderBinding.inflate(inflater,container,false)
    }
}