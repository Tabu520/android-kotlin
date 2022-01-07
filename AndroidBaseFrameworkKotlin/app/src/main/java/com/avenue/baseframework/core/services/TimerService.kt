package com.avenue.baseframework.core.services

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import java.util.*

abstract class TimerService: Service() {

    var mHandler = Handler(Looper.getMainLooper())
    var mTimer: Timer? = null
    open var mTimerTask: TimerTask? = null
    var mDurationFixedRate: Long = 0
    var mTaskStarted = false

    override fun onCreate() {
        super.onCreate()
        if (mTimer != null) {
            mTimer!!.cancel()
        } else {
            mTimer = Timer()
        }
        mTimerTask = LocalTimerTask()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!mTaskStarted) {
            mTimer!!.scheduleAtFixedRate(mTimerTask, mDurationFixedRate, mDurationFixedRate)
            mTaskStarted = true
        }
        return super.onStartCommand(intent, flags, startId)
    }

    abstract fun onTimeOutAction()

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    inner class LocalTimerTask : TimerTask() {
        override fun run() {
            mTaskStarted = false
            mHandler.post { onTimeOutAction() }
        }
    }
}