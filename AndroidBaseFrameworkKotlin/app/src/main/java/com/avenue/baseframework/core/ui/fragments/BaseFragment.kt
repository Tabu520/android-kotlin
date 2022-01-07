package com.avenue.baseframework.core.ui.fragments

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import com.avenue.baseframework.R
import com.avenue.baseframework.core.helpers.EString

open class BaseFragment : Fragment() {

    var titleText: String = EString.EMPTY
    var subTitleText: String = EString.EMPTY
    var leftText: String = EString.EMPTY
    var rightText: String = EString.EMPTY
    protected var baseListener: BaseFragmentListener? = null

    protected var layoutProgress: FrameLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_base, container, false)
    }

    //region ButtonEvent
    open fun onButtonTouchUpInside(view: View?) {}
    //endregion

    //endregion
    //region StartActivity
    protected open fun startActivity(cls: Class<*>?) {
        val intent = Intent(activity, cls)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        requireActivity().finish()
    }

    protected open fun startActivityClearTop(cls: Class<*>?) {
        val intent = Intent(activity, cls)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        requireActivity().finish()
    }
    //endregion

    //endregion
    //region CreateDialog
    open fun createDialog(message: String?, positive: String?, negative: String?): Dialog? {
        val builder = AlertDialog.Builder(activity)
        builder.setMessage(message)
            .setPositiveButton(positive) { dialog: DialogInterface?, _: Int ->
                onDialogPositiveClick(dialog)
            }
            .setNegativeButton(negative) { dialog: DialogInterface?, _: Int ->
                onDialogNegativeClick(dialog)
            }
        return builder.create()
    }

    open fun createDialog(message: String?, positive: String?): Dialog? {
        val builder = AlertDialog.Builder(activity)
        builder.setMessage(message)
            .setPositiveButton(positive) { dialog: DialogInterface?, _: Int ->
                onDialogPositiveClick(dialog)
            }
        return builder.create()
    }

    open fun createDialog(message: String?): Dialog? {
        val builder = AlertDialog.Builder(activity)
        builder.setMessage(message)
        return builder.create()
    }

    open fun createInputDialog(message: String?, positive: String?, negative: String?): Dialog? {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(message)
        val dialogView: View =
            LayoutInflater.from(activity).inflate(R.layout.layout_dialog_input, null)
        val input = dialogView.findViewById<EditText>(R.id.editText)
        builder.setView(dialogView)
        builder.setPositiveButton(positive) { dialog: DialogInterface?, _: Int ->
            onDialogPositiveClick(dialog, message, input.text.toString())
        }
            .setNegativeButton(negative) { dialog: DialogInterface?, _: Int ->
                onDialogNegativeClick(dialog, message)
            }
        return builder.create()
    }

    protected open fun onInvalidateToken() {
        val builder = AlertDialog.Builder(activity)
        builder.setMessage(R.string.dialog_invalid_token)
            .setPositiveButton(R.string.ok) { _, _ -> }
            .setOnDismissListener { }
        val dialog = builder.create()
        dialog.show()
    }

    open fun onDialogPositiveClick(dialog: DialogInterface?) {}

    open fun onDialogPositiveClick(dialog: DialogInterface?, title: String?, value: String?) {}

    open fun onDialogNegativeClick(dialog: DialogInterface?) {}

    open fun onDialogNegativeClick(dialog: DialogInterface?, title: String?) {}
    //endregion

    //region Keyboard
    open fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    open fun hideSoftKeyboard(activity: Activity) {
        val inputMethodManager =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        val focusedView = activity.currentFocus
        if (focusedView != null) {
            inputMethodManager.hideSoftInputFromWindow(focusedView.windowToken, 0)
        }
    }

    open fun onShowKeyboard(keyboardHeight: Int) {}
    open fun onHideKeyboard() {}
    //endregion


    interface BaseFragmentListener {
        fun onAppBarSubtitleChanged(subTitle: String?)
    }
}