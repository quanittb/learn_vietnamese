package com.mobiai.app.ui.fragment

import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mobiai.app.App
import com.mobiai.app.adapter.TopicAdapter
import com.mobiai.app.model.Topic
import com.mobiai.app.utils.gone
import com.mobiai.app.utils.makeGone
import com.mobiai.app.utils.visible
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
                lesson?.let { LessonFragment.instance(it.topicCode) }?.let { addFragment(it) }
            }
        })
        binding.rcvLesson.adapter = topicAdapter
    }
    private fun initData(){
        binding.frLoading.visible()
        val db = FirebaseDatabase.getInstance()
        val ref = db.getReference(App.TOPIC)
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (userSnapshot in dataSnapshot.children) {
                    val topic = userSnapshot.getValue(Topic::class.java)
                    if (topic != null) {
                        val topicNew = Topic(topic.topicCode,topic.name,topic.numberLesson,topic.urlImg)
                        listLesson.add(topicNew)
                    }
                }
                topicAdapter.setItems(listLesson)
                Handler().postDelayed({
                    binding.frLoading.makeGone()
                },1000)

            }
            override fun onCancelled(error: DatabaseError) {
                Handler().postDelayed({
                    binding.frLoading.makeGone()
                },1000)
                Log.w("TAG", "Failed to read value.", error.toException())
            }
        })
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container,false)
    }
}