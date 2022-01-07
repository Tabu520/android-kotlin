package com.avenue.baseframework.core.models

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.content.pm.PackageInfo
import android.content.pm.PackageManager.NameNotFoundException
import android.util.Base64
import androidx.core.content.pm.PackageInfoCompat
import com.avenue.baseframework.core.BaseApplication
import com.avenue.baseframework.core.helpers.Constants
import com.avenue.baseframework.core.helpers.EString
import com.avenue.baseframework.core.helpers.TEA
import javax.inject.Inject

class UserSettings @Inject constructor() {

    private var context: Context? = null

    @Inject
    lateinit var sharedPref: SharedPreferences

    private var editor: Editor? = null

    init {
        context = BaseApplication.getContext()
        editor = sharedPref.edit()
    }

    //SET//
    fun setNetworkSettings(host: String?, port: String?, webPort: String?) {
        editor!!.putString(EString.CONF_SERVER_HOST, host)
        editor!!.putString(EString.CONF_SERVER_PORT, port)
        editor!!.putString(EString.CONF_SERVER_WEB_PORT, webPort)
        editor!!.commit()
    }

    //GET//
    fun getServerHost(): String? {
        return sharedPref.getString(EString.CONF_SERVER_HOST, EString.EMPTY)
    }

    fun getServerPort(): String? {
        return sharedPref.getString(EString.CONF_SERVER_PORT, EString.EMPTY)
    }

    fun getServerWebPort(): String? {
        return sharedPref.getString(EString.CONF_SERVER_WEB_PORT, EString.EMPTY)
    }

    fun getUserName(): String? {
        return sharedPref.getString(EString.CONF_USERNAME, EString.EMPTY)
    }

    fun getPassword(): String? {
        return sharedPref.getString(EString.CONF_PASSWORD, EString.EMPTY)
    }

    fun getDeviceID(): String? {
        return sharedPref.getString(EString.CONF_DEVICE_ID, "invalid")
    }

    fun getUniqueID(): String? {
        return sharedPref.getString(EString.CONF_UNIQUE_ID, "invalid")
    }

    fun getLanguage(): String? {
        return sharedPref.getString(EString.CONF_LANGUAGE, EString.EN)
    }

    fun setLanguage(language: String?) {
        editor!!.putString(EString.CONF_LANGUAGE, language)
        editor!!.commit()
    }

    fun setUsernamePassword(username: String?, password: String?) {
        editor!!.putString(EString.CONF_USERNAME, username)
        editor!!.putString(EString.CONF_PASSWORD, password)
        editor!!.commit()
    }

    fun setUserInfo(username: String?, password: String?) {
        editor!!.putString(EString.CONF_USERNAME, username)
        editor!!.putString(EString.CONF_PASSWORD, password)
        editor!!.commit()
    }

    fun setUserInfo(info: String) {
        val cipher = getDeviceID() + getUniqueID()
        val tea = TEA(cipher.toByteArray())
        val original = info.toByteArray()
        val crypt: ByteArray = tea.encrypt(original)
        val userInfo = Base64.encodeToString(crypt, 0)
        editor!!.putString(EString.CONF_USER_INFO, userInfo)
        editor!!.commit()
    }

    fun getUserInfo(): String {
        var userInfo = sharedPref.getString(EString.CONF_USER_INFO, "invalid")
        val cipher = getDeviceID() + getUniqueID()
        val tea = TEA(cipher.toByteArray())
        if (userInfo != "invalid") {
            val decodeByte: ByteArray = tea.decrypt(Base64.decode(userInfo, 0))
            userInfo = String(decodeByte)
        }
        return userInfo
    }

    fun getAppSettingPath(): String? {
        return sharedPref.getString(Constants.CONF_APP_SETTING_PATH, EString.EMPTY)
    }

    fun setAppSettingPath(path: String?) {
        editor!!.putString(Constants.CONF_APP_SETTING_PATH, path)
        editor!!.commit()
    }

    fun getAppVersion(): String {
        var version = "1.0.0"
        var longVersionCode: Long = 100
        try {
            val pInfo: PackageInfo = context!!.packageManager.getPackageInfo(
                BaseApplication.getContext().packageName,
                0
            )
            version = pInfo.versionName
            longVersionCode = PackageInfoCompat.getLongVersionCode(pInfo)
        } catch (e: NameNotFoundException) {
            e.printStackTrace()
        }
        return "$version (build: $longVersionCode)"
    }
}