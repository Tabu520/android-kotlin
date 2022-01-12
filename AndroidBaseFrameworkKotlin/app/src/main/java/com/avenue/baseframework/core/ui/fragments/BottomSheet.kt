package com.avenue.baseframework.core.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.avenue.baseframework.R
import com.github.florent37.viewanimator.ViewAnimator
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

open class BottomSheet(
    private val callback: BottomSheetCallback
) : BottomSheetDialogFragment() {

    protected open var isAnimation = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.bottomsheet_dialog, container, false)

        val btnPositive = view.findViewById<Button>(R.id.btnPositive)
        btnPositive.setOnClickListener { onButtonClicked(btnPositive) }

        val btnNegative = view.findViewById<Button>(R.id.btnNegative)
        btnNegative.setOnClickListener { onButtonClicked(btnNegative) }

        return view
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
            .accelerate().duration(duration.toLong()).onStart {
                isAnimation = true
            }.onStop {
                onButtonTouchUpInside(view)
                view.alpha = alphaFrom
                isAnimation = false
            }.start()
    }

    open fun onButtonTouchUpInside(view: View) {
        if (view.id == R.id.btnPositive) {
            callback.onPositive()
        } else if (view.id == R.id.btnNegative) {
            callback.onNegative()
        }
    }


    interface BottomSheetCallback {
        fun onPositive()
        fun onNegative()
    }
}