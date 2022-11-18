package com.taipt.mp3tik.utils

import com.taipt.mp3tik.BuildConfig

object Logger {

    private const val TAG = "PTTTag"

    fun logMessage(message: String) {
        if (BuildConfig.DEBUG) {
            System.err.println("PTTTag $message")
        }
    }
}