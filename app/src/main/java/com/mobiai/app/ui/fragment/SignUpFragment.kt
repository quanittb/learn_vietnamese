package com.mobiai.app.ui.fragment

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.mobiai.app.model.User
import com.mobiai.base.basecode.storage.SharedPreferenceUtils
import com.mobiai.base.basecode.ui.fragment.BaseFragment
import com.mobiai.databinding.SignUpFragmentBinding
import java.util.Objects

class SignUpFragment : BaseFragment<SignUpFragmentBinding>() {

    companion object{
        val USER = "user"
        fun instance() : SignUpFragment{
            return newInstance(SignUpFragment::class.java)
        }
    }
    private lateinit var user:User
    override fun initView() {
        binding.tvSignIn.setOnClickListener {
            replaceFragment(SignInFragment.instance())
        }

        binding.btnSignUp.setOnClickListener {
            //createUser()
            createUserRealtime()
           // getUserRealtime()
        }
    }
    private fun createUserRealtime(){
        val db = FirebaseDatabase.getInstance()
       val ref = db.getReference(USER)
        val email = binding.inputEmail.text.toString().trim()
        val password = binding.inputPass.text.toString().trim()
        val name = binding.inputFistName.text.toString().trim()
        user = User(email = email,name = name, pass = password)
        ref.setValue(user)
        createUser(email,password)
        SharedPreferenceUtils.emailLogin = email
        replaceFragment(HomeFragment.instance())
    }
   private fun getUserRealtime(){
        val db = FirebaseDatabase.getInstance()
       val ref = db.getReference(USER)
       // Read from the database
       ref.addValueEventListener(object : ValueEventListener {
           override fun onDataChange(dataSnapshot: DataSnapshot) {
               // This method is called once with the initial value and again
               // whenever data at this location is updated.
               val value = dataSnapshot.children
               for (i in value){
                   Log.d("TAG", "onDataChange: $i")
               }

           }

           override fun onCancelled(error: DatabaseError) {
               // Failed to read value
               Log.w("TAG", "Failed to read value.", error.toException())
           }
       })
    }

    private fun createUser(email:String , pass:String){
        val auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email, pass)
    }


    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): SignUpFragmentBinding {
        return SignUpFragmentBinding.inflate(inflater, container,false)
    }
}