package com.avenue.baseframework.core.ui.activities.network

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import com.avenue.baseframework.R
import com.avenue.baseframework.core.helpers.EString
import com.avenue.baseframework.core.ui.activities.BaseActivity
import com.avenue.baseframework.core.ui.activities.login.LoginActivity
import com.avenue.baseframework.databinding.ActivityNetworkBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NetworkActivity : BaseActivity() {

    private lateinit var binding: ActivityNetworkBinding

    private val networkViewModel: NetworkViewModel by viewModels()

    private var isRequestPermission = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNetworkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        networkViewModel.mHost.observe(this, {
            binding.editTextHost.setText(it)
        })
        networkViewModel.mPort.observe(this, {
            binding.editTextPort.setText(it)
        })
        networkViewModel.mWebPort.observe(this, {
            binding.editTextWebPort.setText(it)
        })
        networkViewModel.networkFormState.observe(this, {
            if (it == null) return@observe
            binding.loading.visibility = View.GONE
            if (it.isDataValid) {
                if (isStoragePermissionGranted()) {
                    startActivity(LoginActivity::class.java)
                } else {
                    isRequestPermission = true
                }
            } else {
                createDialog(
                    getString(R.string.error_network_failed),
                    getString(R.string.ok)
                )!!.show()
            }
        })
    }

    override fun onButtonTouchUpInside(view: View?) {
        super.onButtonTouchUpInside(view)
        hideSoftKeyboard(this)
        if (view!!.id == R.id.buttonStart) {
            val host: String = binding.editTextHost.text.toString().trim { it <= ' ' }
            val port: String = binding.editTextPort.text.toString().trim { it <= ' ' }
            val webPort: String = binding.editTextWebPort.text.toString().trim { it <= ' ' }
            when {
                host == EString.EMPTY -> binding.editTextHost.error =
                    getString(R.string.error_required)
                port == EString.EMPTY -> binding.editTextPort.error =
                    getString(R.string.error_required)
                webPort == EString.EMPTY -> binding.editTextWebPort.error =
                    getString(R.string.error_required)
                else -> {
                    binding.loading.visibility = View.VISIBLE
                    networkViewModel.validateServer(host, port, webPort)
                }
            }
        }
    }

    private fun isStoragePermissionGranted(): Boolean {
        return if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            true
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                1
            )
            false
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v("NetworkActivity", "Permission: " + permissions[0] + "was " + grantResults[0])
        }
        if (isRequestPermission) {
            isRequestPermission = false
            startActivity(LoginActivity::class.java)
        }
    }
}