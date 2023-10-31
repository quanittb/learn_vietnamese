package com.mobiai.app.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.mobiai.base.basecode.ui.fragment.BaseFragment

class QuestionPagerAdapter(fragmentManager: FragmentManager?, fragmentList: List<BaseFragment<*>>, context: Context) :
    FragmentStatePagerAdapter(fragmentManager!!, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    var fragmentList: List<BaseFragment<*>>
    var context: Context
    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return ""
    }

    init {
        this.fragmentList = fragmentList
        this.context = context
    }
}