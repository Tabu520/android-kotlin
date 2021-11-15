package taipt4.kotlin.activityresultpermission

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import taipt4.kotlin.activityresultpermission.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    val takePicture: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {

                val requestCode: Int = result.data!!.getIntExtra("requestCode", 0)
                Log.d("TaiPT4", "request Code == $requestCode");
                Toast.makeText(applicationContext, requestCode.toString(), Toast.LENGTH_SHORT)
                    .show()
            }

        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        var fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        var firstFragment: FirstFragment = FirstFragment.newInstance()
        fragmentTransaction.add(R.id.first_fragment, firstFragment)
        fragmentTransaction.commit()

        binding.btnNext.setOnClickListener {

        }

        binding.btnCamera.setOnClickListener {
            Toast.makeText(applicationContext, "Camera button is clicked!", Toast.LENGTH_SHORT)
                .show()
            RequestPermissions.requestPermissions(this, object : RequestPermissionCallback {
                override fun onGranted() {
                    Toast.makeText(
                        applicationContext,
                        "Camera permission is granted!",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(this@MainActivity, SecondActivity::class.java).apply {
                        putExtra("requestCode", 100)
                    }
                    takePicture.launch(intent);
                }

                override fun onDenied() {
                    Toast.makeText(
                        applicationContext,
                        "Camera permission is denied!",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }, arrayListOf(Manifest.permission.CAMERA))


        }

        binding.btnStorage.setOnClickListener {
            Toast.makeText(applicationContext, "Storage button is clicked!", Toast.LENGTH_SHORT)
                .show()
            RequestPermissions.requestPermissions(
                this, object : RequestPermissionCallback {
                    override fun onGranted() {
                        Toast.makeText(
                            applicationContext,
                            "Storage permission is granted!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onDenied() {
                        Toast.makeText(
                            applicationContext,
                            "Storage permission is denied!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }, arrayListOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
        }
    }
}