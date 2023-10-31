package com.mobiai.app.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mobiai.base.basecode.ui.fragment.BaseFragment
import com.mobiai.databinding.FragmentResultBinding

class ResultFragment : BaseFragment<FragmentResultBinding>() {
    companion object{
        const val NUMBER_CORRECT = "NUMBER_CORRECT"
        fun instance(numberCorrect:Int, ): ResultFragment{
            Bundle().apply {
                putInt(NUMBER_CORRECT,numberCorrect)
                return newInstance(ResultFragment::class.java,this)
            }
        }
    }
    var numberCorrect  = 0
    var ruby = 0
    var xp = 0
    override fun initView() {
        arguments.let {
            if(it != null){
                numberCorrect = it.getInt(NUMBER_CORRECT)
                ruby = 50 * numberCorrect
                xp = 100 * numberCorrect
            }
            binding.txtRuby.text = "$ruby"
            binding.txtExperience.text = "$xp"
        }
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentResultBinding {
        return FragmentResultBinding.inflate(inflater,container,false)
    }
}