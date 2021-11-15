package taipt4.kotlin.example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import taipt4.kotlin.example.`interface`.OneFragmentListener

class MainActivity : AppCompatActivity(), OneFragmentListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val oneFragment = OneFragment.newInstance("1", "2")
        if (findViewById<FrameLayout>(R.id.contentFrame) != null) {
            if (savedInstanceState != null) {
                supportFragmentManager.executePendingTransactions()
                val fragmentById: Fragment = supportFragmentManager.findFragmentById(R.id.contentFrame) as Fragment
                supportFragmentManager.beginTransaction().remove(fragmentById).commit()
            }
            supportFragmentManager.beginTransaction().add(R.id.contentFrame, oneFragment).commit()
        } else {
            if (savedInstanceState != null) {
                supportFragmentManager.executePendingTransactions()
                val firstFragmentById = supportFragmentManager.findFragmentById(R.id.firstFrame)
                if (firstFragmentById != null) {
                    supportFragmentManager.beginTransaction().remove(firstFragmentById).commit()
                }
                val secondFragmentById = supportFragmentManager.findFragmentById(R.id.secondFrame)
                if (secondFragmentById != null) {
                    supportFragmentManager.beginTransaction().remove(secondFragmentById).commit()
                }
                supportFragmentManager.beginTransaction().add(R.id.firstFrame, oneFragment).commit()
            }
        }

    }

    override fun onItemOnePressed(content: String) {
        val twoFragment = TwoFragment.newInstance("This is Two Fragment")
        if (findViewById<FrameLayout>(R.id.contentFrame) != null) {
            supportFragmentManager.beginTransaction().replace(R.id.contentFrame, twoFragment).addToBackStack(null).commit()
        } else {
            supportFragmentManager.beginTransaction().replace(R.id.secondFrame, twoFragment).commit()
        }
    }
}