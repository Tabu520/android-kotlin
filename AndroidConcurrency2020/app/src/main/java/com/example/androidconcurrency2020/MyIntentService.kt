package com.example.androidconcurrency2020

import android.app.Activity
import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.ResultReceiver
import android.util.Log
import androidx.core.app.JobIntentService
import java.net.URL
import java.nio.charset.Charset

private const val EXTRA_FILE_URL = "com.example.androidconcurrency2020.FILE_URL"

private const val JOB_ACTION = "com.example.androidconcurrency2020.JOB_ACTION"
private const val JOB_ID = 1995

/**
 * An [IntentService] subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
class MyIntentService : JobIntentService() {

    override fun onHandleWork(intent: Intent) {
        when (intent.action) {
            JOB_ACTION -> {
                val url = URL(intent.getStringExtra(EXTRA_FILE_URL))
                val contents = url.readText(Charset.defaultCharset())
                Log.i(LOG_TAG, contents)

                val bundle = Bundle()
                bundle.putString(FILE_CONTENTS_KEY, contents)
                val receiver = intent.getParcelableExtra<ResultReceiver>(RECEIVER_KEY)
                receiver?.send(Activity.RESULT_OK,  bundle)
            }
        }
    }


    companion object {
        @JvmStatic
        fun startAction(context: Context, fileUrl: String, receiver: ResultReceiver) {
            val intent = Intent(context, MyIntentService::class.java).apply {
                action = JOB_ACTION
                putExtra(RECEIVER_KEY, receiver)
                putExtra(EXTRA_FILE_URL, fileUrl)
            }
            enqueueWork(context, MyIntentService::class.java, JOB_ID, intent)
        }
    }
}