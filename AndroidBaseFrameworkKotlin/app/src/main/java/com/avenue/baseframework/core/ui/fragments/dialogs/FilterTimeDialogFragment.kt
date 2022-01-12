package com.avenue.baseframework.core.ui.fragments.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import com.avenue.baseframework.R
import com.avenue.baseframework.core.ui.fragments.dialogs.common.BaseDialogFragment
import java.util.*

class FilterTimeDialogFragment : BaseDialogFragment() {

    private var mOkButtonListener: View.OnClickListener? = null
    private lateinit var mManualTimesFromArray: List<String>
    private lateinit var mDisplayFilterManualTimes: MutableList<String>
    private lateinit var mFilterManualTimes: MutableList<String>
    private var mSelectedHour = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mManualTimesFromArray = resources.getStringArray(R.array.FilterManualTime_Data).toList()

        mDisplayFilterManualTimes = ArrayList()
        mFilterManualTimes = ArrayList()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setContentView(R.layout.dialog_single_choice)
        dialog.setTitle("Filter Locations by LogSheet Time")

        val mChoicesList: ListView = dialog.findViewById(R.id.dialog_single_choice_list)
        mChoicesList.adapter = ArrayAdapter<Any?>(
            context!!,
            android.R.layout.select_dialog_singlechoice,
            mDisplayFilterManualTimes.toTypedArray()
        )

        mChoicesList.onItemClickListener =
            OnItemClickListener { _: AdapterView<*>?, _: View?, position: Int, _: Long ->
                mSelectedHour = mFilterManualTimes[position].toInt()
            }

        for (i in mFilterManualTimes.indices) {
            if (mFilterManualTimes[i].toInt() == mSelectedHour) {
                mChoicesList.setItemChecked(i, true)
                break
            }
        }

        val okButton: Button = dialog.findViewById(R.id.btn_dialog_single_choice_ok)
        okButton.setOnClickListener(mOkButtonListener ?: mPositiveListener)

        val cancelButton: Button = dialog.findViewById(R.id.btn_dialog_single_choice_cancel)
        cancelButton.setOnClickListener(mNegativeListener)

        return dialog
    }

    fun appendPositiveButtonListener(listener: View.OnClickListener) {
        mOkButtonListener = View.OnClickListener { view: View? ->
            listener.onClick(view)
            mPositiveListener!!.onClick(view)
        }
    }
}