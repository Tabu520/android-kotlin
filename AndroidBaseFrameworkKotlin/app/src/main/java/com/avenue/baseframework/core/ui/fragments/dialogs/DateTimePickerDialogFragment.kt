package com.avenue.baseframework.core.ui.fragments.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.DatePicker
import android.widget.TimePicker
import com.avenue.baseframework.R
import com.avenue.baseframework.core.ui.fragments.dialogs.common.BaseDialogFragment
import java.io.Serializable
import java.util.*

open class DateTimePickerDialogFragment: BaseDialogFragment() {

    private lateinit var mSelectedDateTime: Calendar
    private var mListener: OnDateTimePickedListener? = null
    private lateinit var dateTimePickerDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSelectedDateTime = Calendar.getInstance()
        mSelectedDateTime.time = Date()
        if (savedInstanceState != null) {
            mListener =
                savedInstanceState.getSerializable(LISTENER_KEY) as OnDateTimePickedListener?
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dateTimePickerDialog = Dialog(activity!!, theme)
        dateTimePickerDialog.setContentView(R.layout.dialog_datetime_picker)
        dateTimePickerDialog.setTitle(getString(R.string.title_dialog_datetime_picker))

        val setButton = dateTimePickerDialog.findViewById<Button>(R.id.btn_date_time_set)
        setButton.setOnClickListener {
            //validate on method onDateTimePicked of listener in case of needing validate value
            mListener?.let {
                mSelectedDateTime[Calendar.SECOND] = 0
                mSelectedDateTime[Calendar.MILLISECOND] = 0
                it.onDateTimePicked(mSelectedDateTime.time)
            }
            dismiss()
        }

        val cancelButton = dateTimePickerDialog.findViewById<Button>(R.id.btn_date_time_cancel)
        cancelButton.setOnClickListener { dismiss() }

        return dateTimePickerDialog
    }

    override fun onStart() {
        super.onStart()
        setupPickers()
    }

    override fun onResume() {
        super.onResume()
        val window = dateTimePickerDialog.window
        window?.let {
            val lp = WindowManager.LayoutParams()
            lp.copyFrom(it.attributes)
            lp.width = WindowManager.LayoutParams.MATCH_PARENT
            it.attributes = lp
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(LISTENER_KEY, mListener)
    }

    private fun setupPickers() {
        val datePicker: DatePicker = requireDialog().findViewById(R.id.date_picker)
        datePicker.init(
            mSelectedDateTime[Calendar.YEAR],
            mSelectedDateTime[Calendar.MONTH],
            mSelectedDateTime[Calendar.DAY_OF_MONTH]
        ) { _: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
            mSelectedDateTime[year, monthOfYear] = dayOfMonth
        }
        val timePicker: TimePicker = requireDialog().findViewById(R.id.time_picker)
        timePicker.hour = mSelectedDateTime[Calendar.HOUR_OF_DAY]
        timePicker.minute = mSelectedDateTime[Calendar.MINUTE]
        timePicker.setIs24HourView(true)
        timePicker.setOnTimeChangedListener { _: TimePicker?, hourOfDay: Int, minute: Int ->
            mSelectedDateTime[Calendar.HOUR_OF_DAY] = hourOfDay
            mSelectedDateTime[Calendar.MINUTE] = minute
        }
        onPickersInitialSetup(datePicker, timePicker)
    }

    protected open fun onPickersInitialSetup(datePicker: DatePicker?, timePicker: TimePicker?) {}

    interface OnDateTimePickedListener: Serializable {
        fun onDateTimePicked(dateTime: Date?)
    }

    companion object {
        private const val LISTENER_KEY = "DateTimePickerListener"
    }
}