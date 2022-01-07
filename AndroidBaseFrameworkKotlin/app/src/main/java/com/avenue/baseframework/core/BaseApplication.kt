package com.avenue.baseframework.core

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication: Application() {

    companion object {
        var mContext: Context? = null
        var mCurrentContext: Context? = null

        fun getContext() : Context {
            if (mCurrentContext == null) {
                return mContext!!
            }
            return mCurrentContext!!
        }
    }

    override fun onCreate() {
        super.onCreate()
        mContext = applicationContext
    }

}