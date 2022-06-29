package com.example.androidconcurrency2020

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.ResultReceiver
import android.util.Log
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import com.example.androidconcurrency2020.databinding.ActivityMainBinding

const val MESSAGE_KEY = "MESSAGE_KEY"
const val DICE_NUMBER_KEY = "DICE_NUMBER_KEY"
const val DICE_INDEX_KEY = "DICE_INDEX_KEY"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

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
        val receiver = MyResultReceiver(Handler(Looper.getMainLooper()))
        MyIntentService.startAction(this, FILE_URL, receiver)
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
        Handler().post { binding.scrollView.fullScroll(ScrollView.FOCUS_DOWN) }
    }

    private inner class MyResultReceiver(handler: Handler): ResultReceiver(handler) {
        override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
            if (resultCode == Activity.RESULT_OK) {
                val fileContents = resultData?.getString(FILE_CONTENTS_KEY) ?: "Null"
                log(fileContents)
            }
        }
    }

}