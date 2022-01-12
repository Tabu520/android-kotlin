package com.avenue.baseframework.core.ui.fragments.dialogs.common

import android.app.Dialog
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.avenue.baseframework.R

open class BaseDialogFragment: DialogFragment() {

    var mPositiveListener: View.OnClickListener? = null
        get() {
            return if (field == null) {
                field = View.OnClickListener {
                    Log.d("eLogBook", "ELBDialogSupportedFragment::getPositiveListener() -- null")
                    val result = Bundle()
                    result.putString("result", "positive")
                    parentFragmentManager.setFragmentResult("login-key", result)
                    dismiss()
                }
                field
            } else {
                View.OnClickListener { view: View? ->
                    Log.d(
                        "eLogBook",
                        "ELBDialogSupportedFragment::getPositiveListener() -- not null"
                    )
                    field!!.onClick(view)
                    dismiss()
                }
            }
        }
    var mNegativeListener:View.OnClickListener? = null
        get() {
            if (field == null) {
                field = View.OnClickListener {
                    Log.d("eLogBook", "ELBDialogSupportedFragment::getNegativeListener()")
                    val result = Bundle()
                    result.putString("result", "negative")
                    parentFragmentManager.setFragmentResult("login-key", result)
                    dismiss()
                }
            }
            return field
        }


    init {
        arguments = Bundle()
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        dialog?.let {
            setupELogThemeForDialog(resources, it)
            it.setCanceledOnTouchOutside(false)
        }
    }

    fun setupELogThemeForDialog(res: Resources, dialog: Dialog) {
        val titleId = res.getIdentifier("title", "id", "android")
        val title = dialog.findViewById<View>(titleId) as TextView
        title.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimaryLight))

        val alertTitleId = res.getIdentifier("alertTitle", "id", "android")
        val alertTitle = dialog.findViewById<View>(alertTitleId) as TextView
        alertTitle.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimaryLight))

        val dividerId = res.getIdentifier("titleDivider", "id", "android")
        val divider = dialog.findViewById<View>(dividerId)
        divider?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorPrimaryLight))
    }
}