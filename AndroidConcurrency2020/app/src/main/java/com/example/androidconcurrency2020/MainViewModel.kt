package com.example.androidconcurrency2020

import android.os.Bundle
import android.os.Message
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import java.net.URL
import java.nio.charset.Charset
import kotlin.concurrent.thread
import kotlin.random.Random

class MainViewModel: ViewModel() {

    val dieValue = MutableLiveData<Pair<Int, Int>>()

    fun rollTheDice() {
        for (dieIndex in 0 until 5) {
            viewModelScope.launch {
                for (i in 1..20) {
                    val number = getDieValue()
                    dieValue.value = Pair(dieIndex, number)
                    Log.i(LOG_TAG, "rollTheDice: $dieIndex -- $number")
                    delay(100)
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

}