package com.example.androidconcurrency2020

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.util.Log
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import androidx.work.*
import com.example.androidconcurrency2020.databinding.ActivityMainBinding

const val MESSAGE_KEY = "MESSAGE_KEY"
const val DICE_NUMBER_KEY = "DICE_NUMBER_KEY"
const val DICE_INDEX_KEY = "DICE_INDEX_KEY"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var myService: MyService
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.i(LOG_TAG, "Connecting Service...")
            val binder = service as MyService.MyServiceBinder
            myService = binder.getService()
        }

        override fun onServiceDisconnected(name: ComponentName?) {

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view binding for view object references
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize button click handlers
        with(binding) {
            runButton.setOnClickListener { runCode() }
            clearButton.setOnClickListener { clearOutput() }
        }

    }

    override fun onStart() {
        super.onStart()
        Intent(this, MyService::class.java).also {
            bindService(it, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
    }

    /**
     * Run some code
     */
    private fun runCode() {
//        thread(start = true) {
//            val bundle = Bundle()
//            for (i in 1..10) {
//                bundle.putString(MESSAGE_KEY, "Looping $i")
//                Message().also {
//                    it.data = bundle
//                    handler.sendMessage(it)
//                }
//                Thread.sleep(1000)
//            }
//            bundle.putString(MESSAGE_KEY, "All done!")
//            Message().also {
//                it.data = bundle
//                handler.sendMessage(it)
//            }
//        }

//        CoroutineScope(Dispatchers.Main).launch {
//            val result = fetchSomething()
//            log(result)
//        }
//        val receiver = MyResultReceiver(Handler(Looper.getMainLooper()))
//        MyIntentService.startAction(this, FILE_URL, receiver)

//        val constraints =
//            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
//        val workRequest = OneTimeWorkRequestBuilder<MyWorker>()
//            .setConstraints(constraints)
//            .build()
//        val workManager = WorkManager.getInstance(applicationContext)
//
//        workManager.enqueue(workRequest)
//        workManager.getWorkInfoByIdLiveData(workRequest.id)
//            .observe(this) { workInfo ->
//                if (workInfo.state == WorkInfo.State.SUCCEEDED) {
//                    log("Work finished!")
//                    val result = workInfo.outputData.getString(DATA_KEY)
//                    log(result ?: "Null")
//                }
//            }
        myService.doSomething()
    }

    /**
     * Clear log display
     */
    private fun clearOutput() {
        binding.logDisplay.text = ""
        scrollTextToEnd()
    }

    /**
     * Log output to logcat and the screen
     */
    @Suppress("SameParameterValue")
    private fun log(message: String) {
        Log.i(LOG_TAG, message)
        binding.logDisplay.append(message + "\n")
        scrollTextToEnd()
    }

    /**
     * Scroll to end. Wrapped in post() function so it's the last thing to happen
     */
    private fun scrollTextToEnd() {
        Handler(Looper.getMainLooper()).post { binding.scrollView.fullScroll(ScrollView.FOCUS_DOWN) }
    }

    private inner class MyResultReceiver(handler: Handler) : ResultReceiver(handler) {
        override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
            if (resultCode == Activity.RESULT_OK) {
                val fileContents = resultData?.getString(FILE_CONTENTS_KEY) ?: "Null"
                log(fileContents)
            }
        }
    }

}