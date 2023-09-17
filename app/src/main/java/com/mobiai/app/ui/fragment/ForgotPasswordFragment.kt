package com.mobiai.app.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.mobiai.base.basecode.ui.fragment.BaseFragment
import com.mobiai.databinding.ForgotPasswordFragmentBinding
import com.mobiai.databinding.SignUpFragmentBinding

class ForgotPasswordFragment : BaseFragment<ForgotPasswordFragmentBinding>() {

    companion object{
        fun instance() : ForgotPasswordFragment{
            return newInstance(ForgotPasswordFragment::class.java)
        }
    }
    override fun initView() {
        binding.icBack.setOnClickListener {
            handlerBackPressed()
        }
        binding.btnSendEmail.setOnClickListener {
            val user = FirebaseAuth.getInstance()
           user.sendPasswordResetEmail(binding.inputEmail.text.toString().trim())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(requireContext(),"send success", Toast.LENGTH_SHORT).show()

                    }
                }
        }

    }

    override fun handlerBackPressed() {
        super.handlerBackPressed()
        closeFragment(this)
    }
    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ForgotPasswordFragmentBinding {
        return ForgotPasswordFragmentBinding.inflate(inflater, container,false)
    }
}