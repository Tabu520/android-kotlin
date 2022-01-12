package com.avenue.baseframework.core.receivers

import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.widget.Toast
import com.avenue.baseframework.core.helpers.Constants
import com.avenue.baseframework.core.ui.activities.version.AppVersionActivity
import com.avenue.baseframework.core.ui.activities.version.UpdateAppVersion
import com.avenue.baseframework.core.utils.NetUtils

class NetworkChangeReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent!!.action == Constants.ACTION_CONNECTIVITY_CHANGE) {
            if (NetUtils.isNetworkAvailable()) {
                context?.let {
                    val mWifi =
                        it.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
                    if (mWifi.wifiState == WifiManager.WIFI_STATE_ENABLED) {
                        Toast.makeText(it, "Wifi is turned on", Toast.LENGTH_SHORT).show()
                        val mActivityManager =
                            it.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
                        val topActivity = mActivityManager.appTasks[0].taskInfo.topActivity!!.className
                        if (topActivity != AppVersionActivity::class.java.name) {
                            val updateAppVersion = UpdateAppVersion(it)
                            updateAppVersion.checkAppVersion()
                        }
                    }
                }
            }
        }
    }
}