package com.mobiai.app.ui.fragment

import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mobiai.app.App.Companion.USER
import com.mobiai.app.model.User
import com.mobiai.base.basecode.storage.SharedPreferenceUtils
import com.mobiai.base.basecode.ui.fragment.BaseFragment
import com.mobiai.databinding.SignUpFragmentBinding

class SignUpFragment : BaseFragment<SignUpFragmentBinding>() {

    companion object{
        fun instance() : SignUpFragment{
            return newInstance(SignUpFragment::class.java)
        }
    }
    override fun initView() {
        binding.tvSignIn.setOnClickListener {
            replaceFragment(SignInFragment.instance())
        }

        binding.btnSignUp.setOnClickListener {
            if (isValidSignInDetails()){
                createUserRealtime()
            }
        }
    }
    private fun createUserRealtime(){
        val db = FirebaseDatabase.getInstance()
       val ref = db.getReference(USER)
        val email = binding.inputEmail.text.toString().trim()
        val password = binding.inputPass.text.toString().trim()
        val name = binding.inputFistName.text.toString().trim()
        if (!getUserRealtime()){
            val user = User(email = email,name = name, pass = password)
            ref.child(System.currentTimeMillis().toString()).setValue(user)
            createUser(email,password)
            SharedPreferenceUtils.emailLogin = email
            replaceFragment(BottomNavigationFragment.instance())
        }
        else{
            showToast("your email exists !")
        }
    }
   private fun getUserRealtime():Boolean{
       var isExist = true
        val db = FirebaseDatabase.getInstance()
       val ref = db.getReference(USER)
       val email = binding.inputEmail.text.toString().trim()

       // Read from the database
       ref.addValueEventListener(object : ValueEventListener {
           override fun onDataChange(dataSnapshot: DataSnapshot) {
               for (userSnapshot in dataSnapshot.children) {
                   // Lấy dữ liệu từ mỗi child node và chuyển đổi thành đối tượng User
                   val user = userSnapshot.getValue(User::class.java)
                   if (user != null) {
                       if (user.email == email) {
                           isExist = false
                       }
                   }
               }
           }


           override fun onCancelled(error: DatabaseError) {
               // Failed to read value
               isExist = false
               Log.w("TAG", "Failed to read value.", error.toException())
           }
       })
       return isExist
    }

    private fun createUser(email:String , pass:String){
        val auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email, pass)
    }
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
    private fun isValidSignInDetails(): Boolean {
        // email trống
        return if (binding.inputEmail.text.toString().trim().isEmpty()) {
            showToast("Enter email")
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.text.toString().trim()).matches()) {
            showToast("Enter valid email")
            false
        } else if (binding.inputPass.text.toString().trim().isEmpty()) {
            showToast("Valid password")
            false
        }
        else if (binding.inputEnterPass.text.toString().trim().isEmpty()) {
            showToast("Valid Enter password ")
            false
        }
        else if (binding.inputFistName.text.toString().trim().isEmpty()) {
            showToast("Valid fistName")
            false
        }
       else {
            true
        }
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): SignUpFragmentBinding {
        return SignUpFragmentBinding.inflate(inflater, container,false)
    }
}