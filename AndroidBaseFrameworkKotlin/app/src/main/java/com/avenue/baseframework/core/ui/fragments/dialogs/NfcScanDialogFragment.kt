package com.avenue.baseframework.core.ui.fragments.dialogs

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import com.avenue.baseframework.R
import com.avenue.baseframework.core.ui.fragments.dialogs.common.BaseDialogFragment

class NfcScanDialogFragment : BaseDialogFragment() {

    var mCancelListener: View.OnClickListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(activity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_nfc_scanning)

        val cancelButton = dialog.findViewById<Button>(R.id.btn_nfc_cancel)

        if (mCancelListener == null) {
            mCancelListener = View.OnClickListener { dismiss() }
        }

        cancelButton.setOnClickListener(mCancelListener)

        return dialog
    }
}