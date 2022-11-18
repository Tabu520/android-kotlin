package com.taipt.tiktokdownloaderapp.utils

import com.taipt.tiktokdownloaderapp.BuildConfig

object Logger {

    private const val TAG = "PTTTag"

    fun logMessage(message: String) {
        if (BuildConfig.DEBUG) {
            System.err.println("PTTTag $message")
        }
    }
}