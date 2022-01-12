package com.avenue.baseframework.core.pereference

import android.content.Context
import android.content.res.Resources
import android.content.res.TypedArray
import android.text.format.DateFormat
import android.util.AttributeSet
import android.widget.TimePicker
import androidx.preference.DialogPreference
import androidx.preference.Preference
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class TimePreference @Inject constructor(
    appContext: Context,
    attrs: AttributeSet
) : DialogPreference(
    appContext,
    attrs,
    Resources.getSystem().getIdentifier("dialogPreferenceStyle", "attr", "android")
),
    Preference.OnPreferenceChangeListener {

    private val mPicker: TimePicker? = null
    var DEFAULT_VALUE = Date().time.toString()

    init {
        this.onPreferenceChangeListener = this
        positiveButtonText = "Set"
        negativeButtonText = "Cancel"
    }

    fun setTime(time: Long) {
        persistString(time.toString())
        notifyDependencyChange(shouldDisableDependents())
        notifyChanged()
    }

    fun getPersistedTime(): Calendar? {
        val c = Calendar.getInstance()
        c.timeInMillis = getPersistedString(DEFAULT_VALUE).toLong()
        return c
    }

    private fun updateSummary(time: Long) {
        val dateFormat = DateFormat.getTimeFormat(context)
        val date = Date(time)
        summary = dateFormat.format(date.time)
    }

    override fun onGetDefaultValue(a: TypedArray?, index: Int): Any {
        val defaultValue = a!!.getString(index)
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return try {
            val defaultCalendar = Calendar.getInstance()
            val defaultDate = dateFormat.parse(defaultValue!!)
            defaultCalendar.time = defaultDate!!
            val todayCalendar = Calendar.getInstance()
            todayCalendar[Calendar.HOUR_OF_DAY] = defaultCalendar[Calendar.HOUR_OF_DAY]
            todayCalendar[Calendar.MINUTE] = defaultCalendar[Calendar.MINUTE]
            DEFAULT_VALUE = todayCalendar.timeInMillis.toString()
            DEFAULT_VALUE
        } catch (e: ParseException) {
            e.printStackTrace()
            return Any()
        }
    }

    override fun onSetInitialValue(restorePersistedValue: Boolean, defaultValue: Any?) {
        val time: Long =
            if (restorePersistedValue) getPersistedString(DEFAULT_VALUE).toLong() else DEFAULT_VALUE.toLong()
        setTime(time)
        updateSummary(time)
    }

    override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
        (preference as TimePreference).updateSummary((newValue as Long?)!!)
        return true
    }
}