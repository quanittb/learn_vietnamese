package com.mobiai.app.ui.fragment

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mobiai.app.App
import com.mobiai.app.adapter.TopicAdapter
import com.mobiai.app.model.Topic
import com.mobiai.app.model.User
import com.mobiai.app.utils.gone
import com.mobiai.app.utils.visible
import com.mobiai.base.basecode.storage.SharedPreferenceUtils
import com.mobiai.base.basecode.ui.fragment.BaseFragment
import com.mobiai.databinding.FragmentHomeBinding

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    companion object{
        fun instance() : HomeFragment{
            return newInstance(HomeFragment::class.java)
        }
    }
    lateinit var topicAdapter: TopicAdapter
    var listLesson: ArrayList<Topic> = arrayListOf()

    override fun initView() {
        initAdapter()
        initData()
    }

    private fun initAdapter(){
        topicAdapter =  TopicAdapter(requireContext(), object : TopicAdapter.OnLessonClickListener{
            override fun onClickItemListener(lesson: Topic?) {
                addFragment(LessonFragment.instance())
            }
        })
        binding.rcvLesson.adapter = topicAdapter
    }
    private fun initData(){
        val db = FirebaseDatabase.getInstance()
        val ref = db.getReference(App.TOPIC)
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                binding.frLoading.visible()
                for (userSnapshot in dataSnapshot.children) {
                    val topic = userSnapshot.getValue(Topic::class.java)
                    if (topic != null) {
                        val topicNew = Topic(topic.topicCode,topic.name,topic.numberLesson,topic.urlImg)
                        listLesson.add(topicNew)
                    }
                }
                topicAdapter.setItems(listLesson)
                binding.frLoading.gone()

            }
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException())
            }
        })
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container,false)
    }
}