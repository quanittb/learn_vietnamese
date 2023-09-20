package com.mobiai.app.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import com.mobiai.base.basecode.ui.fragment.BaseFragment
import com.mobiai.databinding.FragmentSettingBinding

class SettingFragment : BaseFragment<FragmentSettingBinding>() {
    companion object{
        fun instance() :SettingFragment {
            return newInstance(SettingFragment::class.java)
        }
    }
    override fun initView() {
        onClick()
    }
    fun onClick(){
        binding.btnReminder.setOnClickListener {
            addFragment(ReminderFragment.instance())
        }
        binding.btnProfile.setOnClickListener {
            addFragment(ProfileFragment.instance())
        }
        binding.btnChangePassword.setOnClickListener {
            
        }
        binding.btnLogout.setOnClickListener {

        }
    }
    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSettingBinding {
        return FragmentSettingBinding.inflate(inflater,container,false)
    }
}