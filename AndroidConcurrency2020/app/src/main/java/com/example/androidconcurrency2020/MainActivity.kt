package com.example.androidconcurrency2020

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.androidconcurrency2020.databinding.ActivityMainBinding
import kotlin.concurrent.thread
import kotlin.random.Random

const val MESSAGE_KEY = "MESSAGE_KEY"
const val DICE_NUMBER_KEY = "DICE_NUMBER_KEY"
const val DICE_INDEX_KEY = "DICE_INDEX_KEY"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
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

        initView()
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        mainViewModel.dieValue.observe(this) {
            imageViews[it.first].setImageResource(drawables[it.second - 1])
        }
        // Initialize button click handlers
        binding.rollButton.setOnClickListener { mainViewModel.rollTheDice() }

    }

    private fun initView() {
        imageViews = arrayOf(binding.die1, binding.die2, binding.die3, binding.die4, binding.die5)
    }



}