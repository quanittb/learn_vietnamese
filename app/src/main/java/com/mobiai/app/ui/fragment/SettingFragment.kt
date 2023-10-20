package com.mobiai.app.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import com.facebook.login.LoginFragment
import com.google.firebase.auth.FirebaseAuth
import com.mobiai.app.utils.setOnSafeClickListener
import com.mobiai.base.basecode.storage.SharedPreferenceUtils
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
        binding.btnReminder.setOnSafeClickListener {
            addFragment(ReminderFragment.instance())
        }
        binding.btnProfile.setOnSafeClickListener {
            addFragment(ProfileFragment.instance())
        }
        binding.btnChangePassword.setOnSafeClickListener {
            addFragment(ChangePasswordFragment.instance())
        }
        binding.btnLogout.setOnSafeClickListener {
            SignOut()
        }
    }

    private fun SignOut() {
        val auth = FirebaseAuth.getInstance()

        auth.signOut()
        // Sau khi đăng xuất, bạn có thể thực hiện các hành động khác ở đây, ví dụ: quay lại màn hình đăng nhập.

        // Ví dụ:
        replaceFragment(SignUpFragment.instance())

        // Xóa thông tin đăng nhập trong SharedPreferences (nếu bạn lưu thông tin đăng nhập bằng SharedPreferences).
        SharedPreferenceUtils.emailLogin = null
    }
    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSettingBinding {
        return FragmentSettingBinding.inflate(inflater,container,false)
    }
}