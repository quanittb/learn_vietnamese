package com.mobiai.app.ui.fragment

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.mobiai.R
import com.mobiai.base.basecode.ui.fragment.BaseFragment
import com.mobiai.databinding.FragmentBottomNavigationBinding

class BottomNavigationFragment : BaseFragment<FragmentBottomNavigationBinding>() {
    companion object{
        fun instance() : BottomNavigationFragment{
            return newInstance(BottomNavigationFragment::class.java)
        }
    }
    private val HOME_FRAGMENT = HomeFragment.instance()
    private val PRONOUNCE_FRAGMENT = PronunciationFragment.instance()
    private val GIFT_FRAGMENT = GiftFragment.instance()
    private val RANK_FRAGMENT = RankFragment.instance()
    private val SETTINGS_FRAGMENT = SettingFragment.instance()
    private var currentFragment  : Fragment = HOME_FRAGMENT

    override fun initView() {
        setListeners()
        requireActivity().supportFragmentManager.beginTransaction().add(binding.layoutAddFragmentMain.id,HOME_FRAGMENT).
        show(HOME_FRAGMENT).commit()
        requireActivity().supportFragmentManager.beginTransaction().add(binding.layoutAddFragmentMain.id,PRONOUNCE_FRAGMENT).
        hide(PRONOUNCE_FRAGMENT).commit()
        requireActivity().supportFragmentManager.beginTransaction().add(binding.layoutAddFragmentMain.id,GIFT_FRAGMENT).
        hide(GIFT_FRAGMENT).commit()
        requireActivity().supportFragmentManager.beginTransaction().add(binding.layoutAddFragmentMain.id,RANK_FRAGMENT).
        hide(RANK_FRAGMENT).commit()
        requireActivity().supportFragmentManager.beginTransaction().add(binding.layoutAddFragmentMain.id,SETTINGS_FRAGMENT).
        hide(SETTINGS_FRAGMENT).commit()
    }
    private fun showFragment(showFragment: Fragment, hideFragment: Fragment){
        requireActivity().supportFragmentManager.beginTransaction().show(showFragment).hide(hideFragment).commit()
    }
    private fun setListeners() {
        binding.bottomNav.selectedItemId = R.id.action_home
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_home -> {
                    if (currentFragment != HOME_FRAGMENT){
                        showFragment(HOME_FRAGMENT,currentFragment)
                        currentFragment = HOME_FRAGMENT
                    }
                }
                R.id.action_pronounce -> {
                    if (currentFragment != PRONOUNCE_FRAGMENT){
                        showFragment(PRONOUNCE_FRAGMENT,currentFragment)
                        currentFragment = PRONOUNCE_FRAGMENT
                    }
                }

                R.id.action_gift -> {
                    if (currentFragment != GIFT_FRAGMENT){
                        showFragment(GIFT_FRAGMENT,currentFragment)
                        currentFragment = GIFT_FRAGMENT
                    }
                }
                R.id.action_rank -> {
                    if (currentFragment != RANK_FRAGMENT){
                        showFragment(RANK_FRAGMENT,currentFragment)
                        currentFragment = RANK_FRAGMENT
                    }
                }
                R.id.action_setting -> {
                    if (currentFragment != SETTINGS_FRAGMENT){
                        showFragment(SETTINGS_FRAGMENT,currentFragment)
                        currentFragment = SETTINGS_FRAGMENT
                    }
                }
            }
            true
        }
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentBottomNavigationBinding {
        return FragmentBottomNavigationBinding.inflate(inflater, container,false)
    }
    override fun handlerBackPressed() {
        super.handlerBackPressed()
        if (requireActivity().supportFragmentManager.backStackEntryCount > 0) {
            requireActivity().supportFragmentManager.popBackStackImmediate();
            requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), android.R.color.transparent);
        }
        if (requireActivity().supportFragmentManager.backStackEntryCount == 0) {
            requireActivity().finishAffinity()
        }
    }

}
