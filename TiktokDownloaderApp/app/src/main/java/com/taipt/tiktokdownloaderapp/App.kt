package com.taipt.tiktokdownloaderapp

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import com.taipt.tiktokdownloaderapp.settings.LocaleHelper
import com.taipt.tiktokdownloaderapp.utils.Logger
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class App: Application() {

    companion object {
        lateinit var localeHelper: LocaleHelper
    }

    override fun attachBaseContext(base: Context?) {
        localeHelper = LocaleHelper(base!!)
        super.attachBaseContext(localeHelper.setLocale(base))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        localeHelper.setLocale(this)
        Logger.logMessage("onConfigurationChanged -- ${newConfig.locale.language}")
    }
}