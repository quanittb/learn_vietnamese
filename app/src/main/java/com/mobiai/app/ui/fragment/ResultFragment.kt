package com.mobiai.app.ui.fragment

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ShareCompat
import androidx.core.content.FileProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mobiai.app.App
import com.mobiai.app.model.AnsweredQuestions
import com.mobiai.app.model.Results
import com.mobiai.app.model.User
import com.mobiai.base.basecode.storage.SharedPreferenceUtils
import com.mobiai.base.basecode.ui.fragment.BaseFragment
import com.mobiai.databinding.FragmentResultBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
class ResultFragment : BaseFragment<FragmentResultBinding>() {
    companion object{
        const val NUMBER_CORRECT = "NUMBER_CORRECT"
        fun instance(numberCorrect:Int,  ): ResultFragment{
            Bundle().apply {
                putInt(NUMBER_CORRECT,numberCorrect)
                return newInstance(ResultFragment::class.java,this)
            }
        }
    }
    var numberCorrect  = 0
    var ruby = 0
    var xp = 0
    private val REQUEST_SCREENSHOT = 123
//    private var listDataReview = arrayListOf<Question>()
    override fun initView() {
        arguments.let {
            if(it != null){
                numberCorrect = it.getInt(NUMBER_CORRECT)
                ruby = 50 * numberCorrect
                xp = 100 * numberCorrect
            }
            binding.txtRuby.text = "$ruby"
            binding.txtExperience.text = "$xp"
            updateResultToFirebase()
            upRubyAndXp()
        }
        binding.ivClose.setOnClickListener{
            replaceFragment(BottomNavigationFragment.instance())
        }
        binding.btnContinue.setOnClickListener{
            replaceFragment(BottomNavigationFragment.instance())
        }
        binding.btnShare.setOnClickListener{
           // takeScreenshot()
            val bitmap =  createBitmapFromView()
            Log.d("TAG", "initView: $bitmap")

        }
    }

    fun shareBitmap(bitmap: Bitmap) {
        val cachePath = File(requireContext().cacheDir, "images")
        cachePath.mkdirs()
        val imageFile = File(cachePath, "image.png")

        try {
            val fileOutputStream = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        // Tạo Uri cho tệp Bitmap tạm thời
        val imageUri = FileProvider.getUriForFile(requireContext(), requireContext().packageName + ".fileprovider", imageFile)

        // Tạo Intent để chia sẻ Bitmap
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "image/*"
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri)

        // Khởi động Intent
        requireContext().startActivity(Intent.createChooser(shareIntent, "Chia sẻ qua"))
        // Xóa tệp tạm thời sau khi chia sẻ
        imageFile.delete()
    }


    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentResultBinding {
        return FragmentResultBinding.inflate(inflater,container,false)
    }
    fun updateResultToFirebase(){
        val codeResult = "${SharedPreferenceUtils.lesssonCode}_${SharedPreferenceUtils.emailLogin}"
        val db = FirebaseDatabase.getInstance()
        val ref = db.getReference(App.RESULTS)
        var i = 0
        var isExist = false
        val resultUpdate = Results(codeResult, SharedPreferenceUtils.emailLogin.toString(), SharedPreferenceUtils.lesssonCode.toString(),numberCorrect)
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (userSnapshot in dataSnapshot.children) {
                    val result = userSnapshot.getValue(AnsweredQuestions::class.java)
                    if (result != null) {
                        if (result.codeResult == resultUpdate.codeResult && i<1) {
                            userSnapshot.key?.let { ref.child(it).setValue(resultUpdate) }
                            i++
                            isExist = true
                            break
                        }
                    }
                }
                if(!isExist){
                    ref.push().setValue(resultUpdate)
                    isExist = true
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("TAG", "Failed to read value.", error.toException())
            }
        })
    }
    private fun upRubyAndXp(){
        val db = FirebaseDatabase.getInstance()
        val ref = db.getReference(App.USER)
        var i = 0
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (userSnapshot in dataSnapshot.children) {
                    val user = userSnapshot.getValue(User::class.java)
                    if (user != null) {
                        if (user.email == SharedPreferenceUtils.emailLogin && i<1) {
                            val userUpdate = User(SharedPreferenceUtils.emailLogin!!,user.name,user.pass,user.urlImage,user.ruby + ruby,user.totalXp + xp)
                            userSnapshot.key?.let { ref.child(it).setValue(userUpdate) }
                            i++
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException())
            }
        })
    }

    fun createBitmapFromView(): Bitmap {
        // Đảm bảo rằng kích thước của Bitmap phù hợp với kích thước của View
        binding.lnResultFragment.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        val width = binding.lnResultFragment.measuredWidth
        val height = binding.lnResultFragment.measuredHeight

        // Tạo Bitmap với kích thước của View
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        // Gắn Bitmap với Canvas để vẽ View lên Bitmap
        val canvas = Canvas(bitmap)
        binding.lnResultFragment.layout(0, 0, width, height)
        binding.lnResultFragment.draw(canvas)
        return bitmap
    }
    private fun saveScreenshot(bitmap: Bitmap): Uri {
        val folder = File(
            Environment.getExternalStorageDirectory().toString() + "/Screenshots"
        )
        folder.mkdirs()

        val file = File(folder, "screenshot.png")

        try {
            val stream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return Uri.fromFile(file)
    }

    private fun shareScreenshot(uri: Uri) {
        val screenshotUri = uri
        ShareCompat.IntentBuilder.from(requireActivity())
            .setType("image/*")
            .setStream(screenshotUri)
            .startChooser()
    }


}