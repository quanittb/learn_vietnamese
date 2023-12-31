package com.mobiai.app.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.Display.Mode
import com.ads.control.admob.AppOpenManager
import com.apero.inappupdate.AppUpdate
import com.apero.inappupdate.AppUpdateManager
import com.google.firebase.database.FirebaseDatabase
import com.mobiai.BuildConfig
import com.mobiai.R
import com.mobiai.app.App
import com.mobiai.app.model.Question
import com.mobiai.app.ui.fragment.BottomNavigationFragment
import com.mobiai.app.ui.fragment.GiftFragment
import com.mobiai.app.ui.fragment.HomeFragment
import com.mobiai.app.ui.fragment.ProfileFragment
import com.mobiai.app.ui.fragment.RankFragment
import com.mobiai.app.ui.fragment.ResultFragment
import com.mobiai.app.ui.fragment.SignInFragment
import com.mobiai.base.basecode.service.db.ModelTestDB
import com.mobiai.base.basecode.service.db.testModelDB
import com.mobiai.base.basecode.storage.SharedPreferenceUtils
import com.mobiai.base.basecode.ui.activity.BaseActivity
import com.mobiai.databinding.ActivityLanguageBinding
import com.mobiai.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>() {
    companion object{
        fun startMain(context: Context, clearTask : Boolean ){
            val intent = Intent(context, MainActivity::class.java).apply {
                if(clearTask){
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            }
            context.startActivity(intent)
        }

    }
    override fun getLayoutResourceId(): Int {
        return R.layout.activity_main
    }

    override fun getViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    override fun createView() {
        AppUpdateManager.getInstance(this).isStartSessionFromOtherApp = false
        AppUpdateManager.getInstance(this).checkUpdateApp(this) {
            AppOpenManager.getInstance().disableAppResume()
        }
        attachFragment()
        this.testModelDB.insertModelTest(ModelTestDB(null, "TEST_INSERT", System.currentTimeMillis()))
    }

    private fun attachFragment(){
        if(BuildConfig.DEBUG){
            addFragment(BottomNavigationFragment.instance())
           // addFragment(ResultFragment.instance(1))

            return
        }
        if (SharedPreferenceUtils.keyUserLogin!=null && SharedPreferenceUtils.emailLogin!=null ){
            addFragment(BottomNavigationFragment.instance())
        }
        else{
            replaceFragment(SignInFragment.instance())
        }
    }
    override fun onResume() {
        super.onResume()
        AppOpenManager.getInstance().disableAppResume()
        AppUpdateManager.getInstance(this).checkNewAppVersionState(this)

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppUpdate.REQ_CODE_VERSION_UPDATE) {
            if (resultCode == Activity.RESULT_OK) {
                AppOpenManager.getInstance().disableAppResume()
            } else {
                if (AppUpdateManager.getInstance(this).getStyleUpdate() == AppUpdateManager.STYLE_FORCE_UPDATE) {
                    AppOpenManager.getInstance().disableAppResume()
                } else {
                    AppOpenManager.getInstance().enableAppResume()
                }
            }
            AppUpdateManager.getInstance(this).onCheckResultUpdate(requestCode, resultCode) {
                AppOpenManager.getInstance().disableAppResume()
            }
        }
    }
}