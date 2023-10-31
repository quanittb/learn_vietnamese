package com.mobiai.app.ui.fragment

import PermissionAlertDialog
import android.content.ContentResolver
import android.net.Uri
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.mobiai.app.App
import com.mobiai.app.model.User
import com.mobiai.app.permission.StoragePermissionUtils
import com.mobiai.app.utils.AppStorageManager.createInternalFolder
import com.mobiai.app.utils.gone
import com.mobiai.app.utils.setOnSafeClickListener
import com.mobiai.app.utils.visible
import com.mobiai.base.basecode.ads.WrapAdsResume
import com.mobiai.base.basecode.extensions.GetDataFromFirebase
import com.mobiai.base.basecode.extensions.LogD
import com.mobiai.base.basecode.extensions.getInfoUser
import com.mobiai.base.basecode.storage.SharedPreferenceUtils
import com.mobiai.base.basecode.ui.fragment.BaseFragment
import com.mobiai.base.basecode.ultility.UriUtils
import com.mobiai.databinding.FragmentProfileBinding
import java.io.File

class ProfileFragment : BaseFragment<FragmentProfileBinding>() {
    companion object{
        fun instance() : ProfileFragment{
            return newInstance(ProfileFragment::class.java)
        }
    }
    private lateinit var db: FirebaseDatabase
    private var fbs = FirebaseStorage.getInstance()
    private lateinit var ref : DatabaseReference
    private var dialogGotoSetting: PermissionAlertDialog? = null
    private var isGoSettingStorageByClickButton = false
    private var tempImageUri: Uri? = null
    private var tempImagePath: String = ""
    private var PACK_FILE_DIR: File? = null

    override fun initView() {
        db = FirebaseDatabase.getInstance()
        ref = db.getReference(App.USER)

        PACK_FILE_DIR = createInternalFolder(requireContext(), SharedPreferenceUtils.emailLogin!!)
        Log.d("TAG", "initView: $PACK_FILE_DIR")
        binding.btnClose.setOnClickListener {
            handlerBackPressed()
        }
        requireContext().getInfoUser(object : GetDataFromFirebase{
            override fun getDataSuccess(user: User) {
                binding.name.text = user.name
                binding.txtRuby.text = user.ruby.toString()
                binding.txtExperience.text = user.totalXp.toString()
                if(isAdded)
                    Glide.with(requireContext()).load(user.urlImage).into(binding.avatar)
            }
            override fun getDataFail(err: String) {
                showToast(err)
            }

        })

        binding.btnEditAvatar.setOnSafeClickListener {
            performClickEditAvt()
        }
    }
    private fun chooseImage() {
        WrapAdsResume.instance.disableAdsResume()
        // imagePicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        galleryLauncher.launch("image/*")
    }

    private val galleryLauncher =
    registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            tempImageUri = uri
            tempImagePath = UriUtils.getPathFromUriTryMyBest(requireContext(), tempImageUri)
            binding.layoutLoading.visible()

            fbs.getReference("profile")
                .putImgToStorage(tempImageUri!!, object : OnPutImageListener {
                    override fun onComplete(url: String) {
                    Glide.with(requireContext()).load(url).into(binding.avatar)
                        saveImageToUserDB(url)
                        binding.layoutLoading.gone()
                        showToast("Update success!")
                    }

                    override fun onFailure(mess: String) {
                        showToast("ERR: $mess")
                    }

                })
        }
    }
    private fun saveImageToUserDB(url: String){
        db = FirebaseDatabase.getInstance()
        ref = db.getReference(App.USER)
        var i = 0
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (userSnapshot in dataSnapshot.children) {
                    val user = userSnapshot.getValue(User::class.java)
                    if (user != null) {
                        if (user.email == SharedPreferenceUtils.emailLogin && i<1) {
                            val userUpdate = User(SharedPreferenceUtils.emailLogin!!,user.name,user.pass,url,user.ruby,user.totalXp)
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

    private fun performClickEditAvt() {
        if (StoragePermissionUtils.isWriteStorageGranted(requireContext())) {
            chooseImage()
        } else {
            StoragePermissionUtils.requestWriteStoragePermission(requestStorageLauncher)
        }
    }

    private val requestStorageLauncher = registerForActivityResult(
    ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            chooseImage()
        } else {
            showDialogGoSettingStorageByClickButton()
        }
    }

    private fun showDialogGoSettingStorageByClickButton() {
        dialogGotoSetting = PermissionAlertDialog(requireContext()) {
            isGoSettingStorageByClickButton = true
            gotoSetting()
        }
        if (!dialogGotoSetting!!.isShowing) {
            dialogGotoSetting!!.show()
        }
    }
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentProfileBinding {
        return FragmentProfileBinding.inflate(inflater,container,false)
    }
    override fun handlerBackPressed() {
        super.handlerBackPressed()
        closeFragment(this)
    }
    fun StorageReference.putImgToStorage(uri: Uri, onPutImage: OnPutImageListener) {

        fun file_extension(uri: Uri): String? {
            val cr: ContentResolver = requireContext().getContentResolver()
            val mime = MimeTypeMap.getSingleton()
            return mime.getExtensionFromMimeType(cr.getType(uri))
        }
        val fileRef: StorageReference = child(
            System.currentTimeMillis().toString() + "." + file_extension(uri!!).toString()
        )
        fileRef.putFile(uri!!).addOnSuccessListener {
            fileRef.downloadUrl.addOnSuccessListener { uri ->
                onPutImage.onComplete(uri.toString())
            }.addOnFailureListener { ex ->
                onPutImage.onFailure(ex.message.toString())
            }
        }
    }
}
    interface OnPutImageListener{
        fun onComplete(url:String)
        fun onFailure(mess:String)
    }