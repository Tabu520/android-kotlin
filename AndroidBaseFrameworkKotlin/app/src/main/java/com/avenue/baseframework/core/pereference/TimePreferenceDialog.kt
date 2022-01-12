package com.avenue.baseframework.core.pereference

import android.content.Context
import android.text.format.DateFormat
import android.view.View
import android.widget.TimePicker
import androidx.preference.PreferenceDialogFragmentCompat
import java.util.*
import android.os.Bundle

class TimePreferenceDialog: PreferenceDialogFragmentCompat() {

    private lateinit var mPicker: TimePicker
    private lateinit var timePreference: TimePreference

    fun newInstance(key: String?): TimePreferenceDialog {
        val fragment = TimePreferenceDialog()
        val b = Bundle(1)
        b.putString(ARG_KEY, key)
        fragment.arguments = b
        return fragment
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (preference is TimePreference) {
            timePreference = preference as TimePreference
        }
    }

    override fun onCreateDialogView(context: Context?): View {
        mPicker = TimePicker(getContext())
        mPicker.setIs24HourView(DateFormat.is24HourFormat(getContext()))
        return mPicker
    }

    override fun onBindDialogView(view: View?) {
        super.onBindDialogView(view)
        val c: Calendar = timePreference.getPersistedTime()!!
        mPicker.hour = c[Calendar.HOUR_OF_DAY]
        mPicker.minute = c[Calendar.MINUTE]
    }

    override fun onDialogClosed(positiveResult: Boolean) {
        if (positiveResult) {
            val c = Calendar.getInstance()
            c[Calendar.MINUTE] = mPicker.minute
            c[Calendar.HOUR_OF_DAY] = mPicker.hour
            if (!timePreference.callChangeListener(c.timeInMillis)) {
                return
            }
            timePreference.setTime(c.timeInMillis)
        }
    }
}