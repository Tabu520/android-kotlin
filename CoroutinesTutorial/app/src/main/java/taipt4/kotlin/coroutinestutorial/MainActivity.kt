package taipt4.kotlin.coroutinestutorial

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"
//    private lateinit var txtDummy: TextView
    private lateinit var btnStart: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        txtDummy = findViewById(R.id.txt_dummy)
        btnStart = findViewById(R.id.btn_start)
        btnStart.setOnClickListener {
            lifecycleScope.launch {
                while (true) {
                    delay(1000L)
                    Log.d(TAG, "Still running!!!")
                }
            }
            GlobalScope.launch {
                delay(5000L)
                Intent(this@MainActivity, SecondActivity::class.java).also {
                    startActivity(it)
                    finish()
                }
            }
        }

//        Log.d(TAG, "Hello from thread ${Thread.currentThread().name}")
//        GlobalScope.launch(Dispatchers.IO) {
//            delay(3000L)
//            Log.d(TAG, "Coroutine says hello from thread ${Thread.currentThread().name}")
//            val networkCall1 = onNetworkCall()
//            val networkCall2 = onNetworkCall2()
//            withContext(Dispatchers.Main) {
//                txtDummy.text = networkCall1
//            }
//            Log.d(TAG, networkCall1)
//            Log.d(TAG, networkCall2)
//        }

//        Log.d(TAG, "Before runBlocking")
//        runBlocking {
//            launch(Dispatchers.IO) {
//                delay(3000L)
//                Log.d(TAG, "Finished IO Coroutine 1")
//            }
//            launch(Dispatchers.IO) {
//                delay(3000L)
//                Log.d(TAG, "Finished IO Coroutine 2")
//            }
//            Log.d(TAG, "start runBlocking")
//            delay(5000L)
//            Log.d(TAG, "end runBlocking")
//        }

//        val job = GlobalScope.launch(Dispatchers.Default) {
//            Log.d(TAG, "Starting long running calculation...")
//            for (i in 30..40) {
//                if (isActive) {
//                    Log.d(TAG, "Result for i = $i : ${fib(i)}")
//                }
//            }
//            Log.d(TAG, "Ending long running calculation...")
//        }
//
//        runBlocking {
//            delay(3000L)
//            job.cancel()
//            Log.d(TAG, "Done")
//        }

//        GlobalScope.launch(Dispatchers.IO) {
//            val time = measureTimeMillis {
//                val answer1 = async { onNetworkCall1() }
//                val answer2 = async { onNetworkCall2() }
//                Log.d(TAG, "Answer 1 is ${answer1.await()}")
//                Log.d(TAG, "Answer 2 is ${answer2.await()}")
//            }
//            Log.d(TAG, "Request took $time ms")
//        }
    }

    private suspend fun onNetworkCall1() :String {
        delay(3000L)
        return "Net work call 1"
    }

    private suspend fun onNetworkCall2() :String {
        delay(3000L)
        return "Net work call 2"
    }

    fun fib(n: Int): Long {
        return if (n == 0) 0
        else if (n == 1) 1
        else fib(n - 1) + fib(n - 2)
    }
}