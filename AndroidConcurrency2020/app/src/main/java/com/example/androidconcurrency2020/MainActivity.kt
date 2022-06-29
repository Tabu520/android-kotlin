package com.example.androidconcurrency2020

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import com.example.androidconcurrency2020.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
        MyIntentService.startActionFoo(this, "Param 1", "Param 2")
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

    private suspend fun fetchSomething(): String {
        delay(2000)
        return "Pham The Tai"
    }

}