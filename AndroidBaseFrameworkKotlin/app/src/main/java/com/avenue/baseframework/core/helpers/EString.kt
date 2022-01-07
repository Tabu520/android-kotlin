package com.avenue.baseframework.core.helpers

import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.URLSpan
import android.widget.TextView
import com.avenue.baseframework.core.BaseApplication

object EString {

    const val EMPTY = ""
    const val EN = "en"
    const val VI = "vi"
    const val ALL = "all"

    const val DIVISION = "Division"
    const val CATEGORY = "Category"
    const val CODE = "CODE"
    const val CEO = "CEO"

    const val PERCENT_MORE = ">100%"

    const val DATE_PATTERN_SUBTITLE = "yyyy-MM-dd'T'HH:mm:ss"
    const val DATE_PATTERN_CURRENT_YEAR = "yyyy-MM-dd"

    const val CONF_DEVICE_ID = "device_id"
    const val CONF_UNIQUE_ID = "unique_id"
    const val CONF_BIOMETRIC = "biometric"
    const val CONF_SERVER_HOST = "server_host"
    const val CONF_SERVER_PORT = "server_port"
    const val CONF_SERVER_WEB_PORT = "server_web_port"
    const val CONF_FIRST_LOGIN = "first_login"
    const val CONF_USER_INFO = "user_info"
    const val CONF_DIVISION_INFO = "division_info"
    const val CONF_OVER_FACTOR_PERCENT = "overfactor_percent"
    const val CONF_USERNAME = "username"
    const val CONF_PASSWORD = "password"
    const val CONF_USER_DIVISION = "userdivision"
    const val CONF_LANGUAGE = "userlanguage"

    const val SERVER_ERROR = "ServerError"

    const val VALID_TOKEN = "VALID TOKEN"
    const val INVALID_TOKEN = "INVALID TOKEN"
    const val LOGIN_SUCCESS = "LOGIN SUCCESS"
    const val LOGIN_FAILED = "LOGIN FAILED"

    const val UNDEFINED = -1001

    fun stripUnderlines(tv: TextView, text: String?) {
        val s: Spannable = SpannableString(text)
        val spans = s.getSpans(0, s.length, URLSpan::class.java)
        for (span in spans) {
            val start = s.getSpanStart(span)
            val end = s.getSpanEnd(span)
            s.removeSpan(span)
            s.setSpan(URLSpanNoUnderline(span.url), start, end, 0)
        }
        tv.text = s
    }

    class URLSpanNoUnderline(url: String?) : URLSpan(url) {
        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isUnderlineText = false
        }
    }

    fun getString(stringID: Int): String {
        return BaseApplication.getContext().getString(stringID)
    }
}