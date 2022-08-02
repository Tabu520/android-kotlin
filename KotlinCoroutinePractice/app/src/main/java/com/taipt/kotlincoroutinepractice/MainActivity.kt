package com.taipt.kotlincoroutinepractice

import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.transform
import java.io.IOException
import kotlin.system.measureTimeMillis

class MainActivity : AppCompatActivity() {

    private lateinit var txtCountDown: TextView
    val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txtCountDown = findViewById(R.id.txt_count_down)
        val time = viewModel.countDownFlow

    }

    fun runTransform() = runBlocking {
        (1..9).asFlow()
            .transform { value ->
                if (value % 2 == 0) {
                    emit(value * value)
                    emit(value * value * value)
                }
            }
            .collect { response ->
                println(response)
            }
    }

    fun foo1(): Sequence<Int> = sequence {
        for (i in 1..3) {
            Thread.sleep(1000)
            yield(i)
        }
    }

    fun foo2(): Flow<Int> = flow {
        for (i in 1..3) {
            delay(1000)
            emit(i)
        }
    }

    private fun runFlow() = runBlocking {
        launch {
            println(Thread.currentThread().name)
            for (k in 1..3) {
                delay(1000)
                println("I'm not blocked $k")
            }
        }
        val time = measureTimeMillis {
            foo2().collect { value -> println(value) }
        }
        println("$time s")
    }

    private fun runSequence() = runBlocking {
        launch {
            println(Thread.currentThread().name)
            for (k in 1..3) {
                delay(1000)
                println("I'm blocked $k")
            }
        }
        val time = measureTimeMillis {
            foo1().forEach { value -> println(value) }
        }
        println("$time s")
    }

    private fun exceptionExample() = runBlocking {
        val handler = CoroutineExceptionHandler { _, exception ->
            println("Caught $exception with suppressed ${exception.suppressed.contentToString()}")
        }
        val job = GlobalScope.launch(handler) {
            launch {
                try {
                    delay(Long.MAX_VALUE)
                } finally {
                    throw ArithmeticException()
                }
            }
            launch {
                try {
                    delay(Long.MAX_VALUE)
                } finally {
                    throw IndexOutOfBoundsException()
                }
            }
            launch {
                delay(100)
                throw IOException()
            }
            delay(Long.MAX_VALUE)
        }
        job.join()
    }

    private fun scopeExample() = runBlocking {
        launch {
            delay(200L)
            println("Task from runBlocking")

            coroutineScope { // coroutine 2   // scope 2
                launch {   // coroutine 3
                    delay(500L)
                    println("Task from nested launch") // line code 2
                }

                delay(100L)
                println("Task from coroutine scope") // line code 3
            }

            println("Coroutine scope is over") // line code 4
        }
    }

    private fun asyncAwait() = runBlocking {
        val time = measureTimeMillis {
            val one = async { printOne() }
            val two = async { printTwo() }
            println("The answer is ${one.await() + two.await()}")
        }
        println("Completed in $time s")
    }

    suspend fun printOne(): Int {
        delay(1000L)
        return 10
    }

    suspend fun printTwo(): Int {
        delay(1000L)
        return 20
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun main() = runBlocking {
        newSingleThreadContext("thread1").use { context1 ->

            //tạo một context là context1 chứ chưa launch coroutine
            //context1 sẽ có 1 element là dispatcher quyết định coroutine sẽ chạy trên thread có tên là thread1
            println("context1 - ${Thread.currentThread().name}")

            newSingleThreadContext("thread2").use { context2 ->
                //tạo một context là context2 chứ vẫn chưa launch coroutine
                //context2 sẽ có 1 element là dispatcher quyết định coroutine sẽ chạy trên thread có tên là thread2
                println("context2 - ${Thread.currentThread().name}")

                //bắt đầu chạy coroutine
                runBlocking(context1) {

                    //coroutine đang chạy trên context context1 và trên thread thread1
                    println("Started in context1 - ${Thread.currentThread().name}")

                    withContext(context2) {
                        println("Working in context2 - ${Thread.currentThread().name}")
                    }

                    // coroutine đã thoát ra block withContext nên sẽ chạy lại với context1 và trên thread thread1
                    println("Back to context1 - ${Thread.currentThread().name}")
                }
            }

            println("out of context2 block - ${Thread.currentThread().name}")
        }
        println("Out of context1 block ${Thread.currentThread().name}")
    }
}