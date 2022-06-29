package com.example.androidconcurrency2020

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.widget.ImageView
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import com.example.androidconcurrency2020.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.concurrent.thread
import kotlin.random.Random

const val MESSAGE_KEY = "MESSAGE_KEY"
const val DICE_NUMBER_KEY = "DICE_NUMBER_KEY"
const val DICE_INDEX_KEY = "DICE_INDEX_KEY"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var imageViews = emptyArray<ImageView>()
    val drawables = arrayOf(
        R.drawable.dice_1,
        R.drawable.dice_2,
        R.drawable.dice_3,
        R.drawable.dice_4,
        R.drawable.dice_5,
        R.drawable.dice_6
    )
    private val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            val result = msg.data
            val dieNumber = result?.getInt(DICE_NUMBER_KEY) ?: 0
            val dieIndex = result?.getInt(DICE_INDEX_KEY) ?: 1
            Log.i(LOG_TAG, "index=$dieIndex, value=$dieNumber")
            imageViews[dieIndex].setImageResource(drawables[dieNumber - 1])
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view binding for view object references
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        initView()

        // Initialize button click handlers
        with(binding) {
            runButton.setOnClickListener { runCode() }
            clearButton.setOnClickListener { clearOutput() }
        }
//        binding.rollButton.setOnClickListener { rollTheDice() }

    }

//    private fun initView() {
//        imageViews = arrayOf(binding.die1, binding.die2, binding.die3, binding.die4, binding.die5)
//    }

    private fun rollTheDice() {
        for (dieIndex in imageViews.indices) {
            thread(start = true) {
                Thread.sleep(dieIndex * 10L)
                val bundle = Bundle()
                bundle.putInt(DICE_INDEX_KEY, dieIndex)
                for (i in 1..20) {
                    val dieNumber = getDieValue()
                    bundle.putInt(DICE_NUMBER_KEY, dieNumber)
                    Message().also {
                        it.data = bundle
                        handler.sendMessage(it)
                    }
                    Thread.sleep(100)
                }
            }
        }
    }

    /**
     * Get a random number from 1 to 6
     */
    private fun getDieValue(): Int {
        return Random.nextInt(1, 7)
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

        CoroutineScope(Dispatchers.Main).launch {
            val result = fetchSomething()
            log(result)
        }
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
