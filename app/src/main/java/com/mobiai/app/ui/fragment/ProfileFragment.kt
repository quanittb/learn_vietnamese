package com.mobiai.app.ui.fragment

import PermissionAlertDialog
import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mobiai.app.App
import com.mobiai.app.model.User
import com.mobiai.app.permission.StoragePermissionUtils
import com.mobiai.app.utils.AppStorageManager.createInternalFolder
import com.mobiai.app.utils.gone
import com.mobiai.app.utils.setOnSafeClickListener
import com.mobiai.app.utils.visible
import com.mobiai.base.basecode.ads.WrapAdsResume
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
        getTotalAndXp()

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

            val success =  saveImageFromUriToFolder("${System.currentTimeMillis()}.jpg", tempImageUri!!)
            if (success!=null) {

                Glide.with(requireContext()).load(tempImagePath).diskCacheStrategy(
                    DiskCacheStrategy.ALL)
                    .skipMemoryCache(true).into(binding.avatar)

                ref.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (userSnapshot in dataSnapshot.children) {
                            binding.frLoading.visible()
                            // Lấy dữ liệu từ mỗi child node và chuyển đổi thành đối tượng User
                            val user = userSnapshot.getValue(User::class.java)
                            if (user != null) {
                                if (user.email == SharedPreferenceUtils.emailLogin) {
                                    val userUpdate = User(SharedPreferenceUtils.emailLogin!!,user.name,user.pass,success.absolutePath)
                                    userSnapshot.key?.let { ref.child(it).setValue(userUpdate) }
                                    break
                                }
                            }
                            binding.frLoading.gone()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Failed to read value
                        Log.w("TAG", "Failed to read value.", error.toException())
                    }
                })

                showToast("update Image done !")
            } else {
                // Có lỗi xảy ra khi lưu ảnh
                showToast("save Image failed !")
            }

        } else {
           showToast("get data Image failed !")
        }
    }

    private fun saveImageFromUriToFolder(customName: String, imageUri: Uri): File? {
        if (PACK_FILE_DIR != null) {
            try {
                val destinationFile = File(PACK_FILE_DIR, customName)
                Log.d("TAG", "saveImageFromUriToFolder: $destinationFile")
                val inputStream = requireContext().contentResolver.openInputStream(imageUri)
                if (inputStream != null) {
                    destinationFile.outputStream().use { output ->
                        inputStream.copyTo(output)
                    }
                    inputStream.close()
                    return destinationFile
                } else {
                    Log.e("TAG", "Failed to open input stream for the URI.")
                }
            } catch (e: Exception) {
                Log.e("TAG", "Error replacing image in folder", e)
            }
        } else {
            Log.e("TAG", "Folder doesn't exist")
        }

        return null
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
    private fun getTotalAndXp(){
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (userSnapshot in dataSnapshot.children) {
                    // Lấy dữ liệu từ mỗi child node và chuyển đổi thành đối tượng User
                    val user = userSnapshot.getValue(User::class.java)
                    if (user != null) {
                        if (user.email == SharedPreferenceUtils.emailLogin) {
                            binding.txtRuby.text = user.ruby.toString()
                            binding.txtExperience.text = user.totalXp.toString()
                            binding.name.text = user.name

                            if (user.urlImage!=null && user.urlImage!!.isNotEmpty()){
                                Glide.with(requireContext()).load(user.urlImage).diskCacheStrategy(
                                    DiskCacheStrategy.ALL)
                                    .skipMemoryCache(true).into(binding.avatar)
                            }
                            binding.frLoading.gone()
                            break
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
}