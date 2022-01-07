package com.avenue.baseframework.core.helpers

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.text.Html
import androidx.core.text.HtmlCompat
import com.avenue.baseframework.core.BaseApplication
import java.util.*

object Functions {

    //region Language
    fun reloadLanguage(languageToLoad: String?) {
        val locale = Locale(languageToLoad!!)
        Locale.setDefault(locale)
        val config = Configuration()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale)
        } else {
            config.locale = locale
        }
        BaseApplication.getContext().createConfigurationContext(config)
    }
    //endregion

    //endregion
    //region Mail
    fun sendMailTo(
        emailAddress: String,
        subject: String?,
        ccList: Array<String>?,
        bccList: Array<String>?,
        body: String?
    ): Boolean {
        var ccList = ccList
        var bccList = bccList
        val toList: String = if (emailAddress == EString.EMPTY) {
            return false
        } else {
            "mailto:$emailAddress"
        }
        ccList?.let {
            ccList = arrayOf("")
        }
        bccList?.let {
            bccList = arrayOf("")
        }
        if (body == null) {
            return false
        }
        val shareIntent = Intent(Intent.ACTION_SENDTO, Uri.parse(toList))
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        shareIntent.putExtra(Intent.EXTRA_CC, ccList)
        shareIntent.putExtra(Intent.EXTRA_BCC, bccList)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            shareIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(body, HtmlCompat.FROM_HTML_MODE_LEGACY))
        } else {
            shareIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(body))
        }
        BaseApplication.getContext().startActivity(shareIntent)
        return true
    }
    //endregion
}