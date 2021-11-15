package taipt4.kotlin.example2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var btnStart: Button
    private lateinit var btnStop: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnStart = findViewById(R.id.startButton)
        btnStop = findViewById(R.id.stopButton)

        btnStart.setOnClickListener(this)
        btnStop.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when(v) {
            btnStart -> {
                startService(Intent(this, NewService::class.java))
                Toast.makeText(this, "Start Service", Toast.LENGTH_LONG).show()
            }
            btnStop -> {
                stopService(Intent(this, NewService::class.java))
                Toast.makeText(this, "Stop Service", Toast.LENGTH_LONG).show()
            }
        }
    }
}