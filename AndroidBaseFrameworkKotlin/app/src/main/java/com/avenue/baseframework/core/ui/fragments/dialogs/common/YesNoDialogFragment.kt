package com.avenue.baseframework.core.ui.fragments.dialogs.common

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.avenue.baseframework.R

class YesNoDialogFragment : BaseDialogFragment() {

    companion object {
        var mIsOneButtonDialog = false
        fun newInstance(
            title: String?,
            message: String?,
            icon: Int,
            isOneButtonDialog: Boolean
        ): YesNoDialogFragment {
            val args = Bundle()
            args.putString("title", title)
            args.putString("message", message)
            args.putInt("icon", icon)
            mIsOneButtonDialog = isOneButtonDialog
            val dialog = YesNoDialogFragment()
            dialog.arguments = args
            return dialog
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val args = arguments
        val title = args!!.getString("title", "")
        val message = args.getString("message", "")
        val iconId = args.getInt("icon")

        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_alert)

        val messageView: TextView = dialog.findViewById(R.id.txt_alert_dialog_message)
        messageView.text = message

        val iconView: ImageView = dialog.findViewById(R.id.iv_alert_dialog_icon)
        iconView.setImageResource(iconId)

        val positiveButton: Button = dialog.findViewById(R.id.btn_alert_dialog_positive_button)
        positiveButton.text = "Yes"
        positiveButton.setOnClickListener(mPositiveListener)

        val negativeButton: Button = dialog.findViewById(R.id.btn_alert_dialog_negative_button)
        negativeButton.text = "No"
        negativeButton.setOnClickListener(mNegativeListener)
        if (mIsOneButtonDialog) {
            negativeButton.visibility = View.INVISIBLE
        }

        return dialog
    }

    override fun onDestroy() {
        super.onDestroy()
        mIsOneButtonDialog = false
    }

}