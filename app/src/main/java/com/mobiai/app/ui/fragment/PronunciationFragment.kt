package com.mobiai.app.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.mobiai.app.adapter.ItemSpacingDecoration
import com.mobiai.app.adapter.PronunciationAdapter
import com.mobiai.app.model.Pronunciation
import com.mobiai.base.basecode.ui.fragment.BaseFragment
import com.mobiai.databinding.FragmentPronunciationBinding

class PronunciationFragment : BaseFragment<FragmentPronunciationBinding>() {
companion object{
    fun instance() : PronunciationFragment{
        return newInstance(PronunciationFragment::class.java)
    }
}
    private var  pronunciationAdapter : PronunciationAdapter? = null

    override fun initView() {
        initData()
    }
    fun initData() {
        pronunciationAdapter = PronunciationAdapter(requireContext())
        val listPronunciation : ArrayList<Pronunciation> = arrayListOf()
        runBackground({
            listPronunciation.add(Pronunciation("Aa","na"))
            listPronunciation.add(Pronunciation("Ăă","năn"))
            listPronunciation.add(Pronunciation("Ââ","nân"))
            listPronunciation.add(Pronunciation("Bb","bờ"))
            listPronunciation.add(Pronunciation("Cc","cờ"))
            listPronunciation.add(Pronunciation("Dd","dê"))
            listPronunciation.add(Pronunciation("Đđ","đê"))
            listPronunciation.add(Pronunciation("Ee","em"))
            listPronunciation.add(Pronunciation("Êê","êm"))
            listPronunciation.add(Pronunciation("Aa","nan"))
            listPronunciation.add(Pronunciation("Ăă","năn"))
            listPronunciation.add(Pronunciation("Ââ","nân"))
            listPronunciation.add(Pronunciation("Bb","bờ"))
            listPronunciation.add(Pronunciation("Cc","cờ"))
            listPronunciation.add(Pronunciation("Dd","dê"))
            listPronunciation.add(Pronunciation("Đđ","đê"))
            listPronunciation.add(Pronunciation("Ee","em"))
            listPronunciation.add(Pronunciation("Êê","êm"))
            listPronunciation.add(Pronunciation("Aa","nan"))
            listPronunciation.add(Pronunciation("Ăă","năn"))
            listPronunciation.add(Pronunciation("Ââ","nân"))
            listPronunciation.add(Pronunciation("Bb","bờ"))
            listPronunciation.add(Pronunciation("Cc","cờ"))
            listPronunciation.add(Pronunciation("Dd","dê"))
            listPronunciation.add(Pronunciation("Đđ","đê"))
            listPronunciation.add(Pronunciation("Ee","em"))
            listPronunciation.add(Pronunciation("Êê","êm"))
            listPronunciation.add(Pronunciation("Aa","nan"))
            listPronunciation.add(Pronunciation("Ăă","năn"))
            listPronunciation.add(Pronunciation("Ââ","nân"))
            listPronunciation.add(Pronunciation("Bb","bờ"))
            listPronunciation.add(Pronunciation("Cc","cờ"))
            listPronunciation.add(Pronunciation("Dd","dê"))
            listPronunciation.add(Pronunciation("Đđ","đê"))
            listPronunciation.add(Pronunciation("Ee","em"))
            listPronunciation.add(Pronunciation("Êê","êm"))
        },{
            pronunciationAdapter?.setItems(listPronunciation)
            binding.recyclerViewPronun.layoutManager = GridLayoutManager(requireContext(),3)
           // binding.recyclerViewPronun.addItemDecoration(ItemSpacingDecoration(7,8))
            binding.recyclerViewPronun.adapter = pronunciationAdapter
        },{
            pronunciationAdapter?.setItems(listPronunciation)
            binding.recyclerViewPronun.layoutManager = GridLayoutManager(requireContext(),3)
            //binding.recyclerViewPronun.addItemDecoration(ItemSpacingDecoration(7,8))
            binding.recyclerViewPronun.adapter = pronunciationAdapter
        })
        binding.btnTest.setOnClickListener{
            replaceFragment(BottomNavigationFragment.instance())
        }

    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPronunciationBinding {
        return FragmentPronunciationBinding.inflate(inflater,container,false)
    }

}