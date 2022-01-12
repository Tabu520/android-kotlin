package com.avenue.baseframework.core.ui.activities.version

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.pm.PackageInfoCompat
import com.avenue.baseframework.core.BaseApplication
import com.avenue.baseframework.core.callback.AppSettingCallback
import com.avenue.baseframework.core.callback.AppVersionCheckCallback
import com.avenue.baseframework.core.helpers.AppSettings
import com.avenue.baseframework.core.helpers.Constants
import com.avenue.baseframework.core.helpers.EString
import com.avenue.baseframework.core.models.UserSettings
import com.avenue.baseframework.core.parser.AppSettingsFileParser
import com.avenue.baseframework.restclient.RestConnector
import javax.inject.Inject

class UpdateAppVersion(private val context: Context) {

    private val TAG = "UpdateAppVersion"

    @Inject
    lateinit var appSettings: AppSettings

    @Inject
    lateinit var userSettings: UserSettings

    @Inject
    lateinit var restConnector: RestConnector

    fun checkAppVersion() {
        checkAppVersion(object : AppVersionCheckCallback {
            override fun checkResult(result: Boolean) {
                if (result) {
                    // create Dialog warn to force user update
                    val intent = Intent(context, AppVersionActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                }
            }
        })
    }

    private fun checkAppVersion(listener: AppVersionCheckCallback) {
        fetchAppSetting(object : AppSettingCallback {
            override fun onFetched() {
                var longVersionCode: Long = 100
                try {
                    val pInfo: PackageInfo = BaseApplication.getContext().packageManager
                        .getPackageInfo(BaseApplication.getContext().packageName, 0)
                    longVersionCode = PackageInfoCompat.getLongVersionCode(pInfo)
                } catch (e: PackageManager.NameNotFoundException) {
                    e.printStackTrace()
                }
                if (appSettings.APP_VERSION_CODE > longVersionCode) {
                    listener.checkResult(true)
                } else {
                    listener.checkResult(false)
                }
            }
        })
    }

    private fun fetchAppSetting(callback: AppSettingCallback?) {
        val downloadPath = "http://" + userSettings.getServerHost()
            .toString() + ":" + userSettings.getServerWebPort()
            .toString() + "/App/" + Constants.APP_SETTING_FILE_NAME
        Log.d(TAG, "downloadXMLTemplate : $downloadPath")
        restConnector.downloadXMLTemplate(
            downloadPath,
            Constants.APP_SETTING_FILE_NAME
        ) { response ->
            if (response != null) {
                userSettings.setAppSettingPath(response)
                loadAppSettings()
            }
            callback?.onFetched()
        }
    }

    private fun loadAppSettings() {
        val path: String = userSettings.getAppSettingPath()!!
        if (path != EString.EMPTY) {
            AppSettingsFileParser(path)
        }
    }
}