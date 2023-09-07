package com.mobiai.app.ui.activity

import com.ads.control.ads.AperoAd
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.mobiai.BuildConfig
import com.mobiai.R
import com.mobiai.app.storage.AdsRemote
import com.mobiai.base.basecode.ads.TypeLoadAds
import com.mobiai.base.basecode.storage.SharedPreferenceUtils
import com.mobiai.base.basecode.ui.activity.splash.BaseSplashActivity3
import com.mobiai.databinding.ActivitySplashBinding

class SplashActivity3(
    override val isShowAdsSplash: Boolean = AdsRemote.showAdsSplash,
    override val idAdsNormal: String = BuildConfig.inter_splash,
    override val typeLoadAdsSplash: String = TypeLoadAds.SAMETIME,
    override val idAdsHighFloor: String = BuildConfig.inter_splash_priority,
    override val idAdsMedium: String = BuildConfig.inter_splash_medium
) : BaseSplashActivity3<ActivitySplashBinding>() {
    override fun openNextScreen() {
        if (SharedPreferenceUtils.languageCode == null) {
            LanguageActivity.start(this, true)
        }  else {
            MainActivity.startMain(this, true)
        }
    }

    override fun actionAfterFetchData() {
        startNotPurchase()

        //TODO init ads native onboarding or native language
    }

    override fun setUpRemoteConfig() {
        //TODO setup remote

        //TODO please use fun fetchDataRemote() to get date from Firebase
        actionAfterFetchData()
    }

    override fun fetchDataRemote(firebaseRemoteConfig: FirebaseRemoteConfig, callback: () -> Unit) {

        //Please setup data before use super fun

        super.fetchDataRemote(firebaseRemoteConfig, callback)
    }

    override fun checkShowSplashFail() {
        when(typeLoadAdsSplash){
            TypeLoadAds.SAMETIME, TypeLoadAds.ALTERNATE  -> {
                AperoAd.getInstance().onCheckShowSplashPriority3WhenFail(this, adAperoMediumCallback, 1000)
            }
            else -> {
                AperoAd.getInstance().onCheckShowSplashWhenFail(this, adCallback, 1000)
            }
        }
    }

    override fun getLayoutResourceId(): Int = R.layout.activity_splash

    override fun getViewBinding(): ActivitySplashBinding = ActivitySplashBinding.inflate(layoutInflater)

    override fun createView() {
    }

}