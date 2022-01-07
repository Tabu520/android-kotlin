package com.avenue.baseframework.core.ui.activities

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.Window
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.avenue.baseframework.R
import com.avenue.baseframework.core.helpers.EString
import com.avenue.baseframework.core.ui.fragments.BaseFragment
import com.github.florent37.viewanimator.ViewAnimator

open class BaseActivity : AppCompatActivity() {

    private var keyboardListenersAttached = false
    private var rootLayout: ViewGroup? = null

    protected var baseFragment: BaseFragment? = null
    protected var fragmentTag: String = EString.EMPTY
    protected var isAnimation = false

    private val keyboardLayoutListener = OnGlobalLayoutListener {
        val heightDiff = rootLayout!!.rootView.height - rootLayout!!.height
        val contentViewTop = window.findViewById<View>(Window.ID_ANDROID_CONTENT).top
        val broadcastManager = LocalBroadcastManager.getInstance(this@BaseActivity)
        if (heightDiff <= contentViewTop + 250) {
            onHideKeyboard()
            val intent = Intent("KeyboardWillHide")
            broadcastManager.sendBroadcast(intent)
        } else {
            val keyboardHeight = heightDiff - contentViewTop
            onShowKeyboard(keyboardHeight)
            val intent = Intent("KeyboardWillShow")
            intent.putExtra("KeyboardHeight", keyboardHeight)
            broadcastManager.sendBroadcast(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
    }

    override fun onBackPressed() {

    }

    //region StartActivity_Fragment
    protected open fun startActivity(cls: Class<*>?) {
        val intent = Intent(this, cls)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }

    protected open fun startFragment(fragment: BaseFragment, tag: String, animation: Boolean) {
        if (fragmentTag == tag) {
            return
        }
        fragmentTag = tag
        setToolbarLayout(fragment)
        val ft = supportFragmentManager.beginTransaction()
        if (animation) {
            ft.setCustomAnimations(R.animator.move_up, R.animator.move_down)
        }
        ft.replace(R.id.layoutContainer, fragment, tag)
        ft.commit()
        fragmentTag = tag
        baseFragment = fragment
    }
    //endregion

    //region ButtonEvent
    open fun onButtonTouchUpInside(view: View?) {
        baseFragment?.onButtonTouchUpInside(view)
    }

    open fun onButtonClicked(view: View) {
        if (isAnimation) {
            return
        }
        val alphaFrom = view.alpha
        val alphaTo = 0.8f
        val scaleFrom = 1.0f
        val scaleTo = 0.9f
        val duration = 150
        ViewAnimator.animate(view).scaleX(scaleFrom, scaleTo).scaleY(scaleFrom, scaleTo)
            .alpha(alphaFrom, alphaTo).decelerate().duration(duration.toLong()).thenAnimate(view)
            .scaleX(scaleTo, scaleFrom).scaleY(scaleTo, scaleFrom).alpha(alphaTo, alphaFrom)
            .accelerate().duration(duration.toLong())
            .onStart {
                isAnimation = true
            }.onStop {
                onButtonTouchUpInside(view)
                view.alpha = alphaFrom
                isAnimation = false
            }.start()
    }

    open fun onButtonAction(view: View) {
        if (isAnimation) {
            return
        }
        onButtonTouchUpInside(view)
        val alphaFrom = view.alpha
        val alphaTo = 0.8f
        val scaleFrom = 1.0f
        val scaleTo = 0.9f
        val duration = 150
        ViewAnimator.animate(view).scaleX(scaleFrom, scaleTo).scaleY(scaleFrom, scaleTo)
            .alpha(alphaFrom, alphaTo).decelerate().duration(duration.toLong()).thenAnimate(view)
            .scaleX(scaleTo, scaleFrom).scaleY(scaleTo, scaleFrom).alpha(alphaTo, alphaFrom)
            .accelerate().duration(duration.toLong())
            .onStart {
                isAnimation = true
            }.onStop {
                view.alpha = alphaFrom
                isAnimation = false
            }.start()
    }
    //endregion

    protected open fun setToolbarLayout(fragment: BaseFragment?) {}

    //region CreateDialog
    open fun createDialog(
        message: String?,
        positive: String?,
        negative: String?,
        positiveColorID: Int,
        negativeColorID: Int,
        view: View?
    ): Dialog? {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(message)
            .setPositiveButton(positive) { dialog: DialogInterface?, _: Int ->
                onDialogPositiveClick(dialog, view)
            }
            .setNegativeButton(negative) { dialog: DialogInterface?, _: Int ->
                onDialogNegativeClick(dialog, view)
            }
        val dialog = builder.create()
        dialog.setOnShowListener {
            val positiveColor =
                if (positiveColorID == 0) R.color.colorTextPrimary else positiveColorID
            val negativeColor =
                if (negativeColorID == 0) R.color.colorTextPrimary else negativeColorID
            dialog.getButton(Dialog.BUTTON_POSITIVE)
                .setTextColor(ContextCompat.getColor(this, positiveColor))
            dialog.getButton(Dialog.BUTTON_NEGATIVE)
                .setTextColor(ContextCompat.getColor(this, negativeColor))
        }
        return dialog
    }

    open fun createDialog(message: String?, positive: String?): Dialog? {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(message)
            .setPositiveButton(positive) { dialog: DialogInterface?, _: Int ->
                onDialogPositiveClick(dialog)
            }
        return builder.create()
    }

    open fun createDialog(message: String?, positive: String?, negative: String?): Dialog? {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(message)
            .setPositiveButton(positive) { dialog: DialogInterface?, _: Int ->
                onDialogPositiveClick(dialog)
            }
            .setNegativeButton(negative) { dialog: DialogInterface?, _: Int ->
                onDialogNegativeClick(dialog)
            }
        return builder.create()
    }

    open fun createDialog(message: String?, positive: String?, view: View?): Dialog? {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(message)
            .setPositiveButton(positive) { dialog: DialogInterface?, _: Int ->
                onDialogPositiveClick(dialog, view)
            }
        return builder.create()
    }

    open fun createDialog(
        message: String?,
        positive: String?,
        negative: String?,
        view: View?
    ): Dialog? {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(message)
            .setPositiveButton(positive) { dialog: DialogInterface?, _: Int ->
                onDialogPositiveClick(dialog, view)
            }
            .setNegativeButton(negative) { dialog: DialogInterface?, _: Int ->
                onDialogNegativeClick(dialog, view)
            }
        return builder.create()
    }

    protected open fun onDialogPositiveClick(dialog: DialogInterface?) {}

    protected open fun onDialogPositiveClick(dialog: DialogInterface?, view: View?) {}

    protected open fun onDialogNegativeClick(dialog: DialogInterface?) {}

    protected open fun onDialogNegativeClick(dialog: DialogInterface?, view: View?) {}
    //endregion

    //region Keyboard
    open fun hideSoftKeyboard(activity: Activity) {
        val inputMethodManager =
            activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        val focusedView = activity.currentFocus
        focusedView?.let {
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    protected open fun attachKeyboardListeners() {
        if (keyboardListenersAttached) {
            return
        }
        rootLayout = findViewById(R.id.container)
        rootLayout!!.viewTreeObserver.addOnGlobalLayoutListener(keyboardLayoutListener)
        keyboardListenersAttached = true
    }

    protected open fun onShowKeyboard(keyboardHeight: Int) {
        baseFragment!!.onShowKeyboard(keyboardHeight)
    }

    protected open fun onHideKeyboard() {
        baseFragment!!.onHideKeyboard()
    }

    open fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
    //endregion

    override fun onDestroy() {
        super.onDestroy()
        if (keyboardListenersAttached) {
            rootLayout!!.viewTreeObserver.removeOnGlobalLayoutListener(keyboardLayoutListener)
        }
    }
}