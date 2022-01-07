package com.avenue.baseframework.core.ui.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageView
import com.avenue.baseframework.R
import com.avenue.baseframework.core.helpers.EString
import com.avenue.baseframework.core.models.UserSettings
import com.avenue.baseframework.databinding.ActivitySplashBinding
import com.avenue.baseframework.restclient.RestConnector
import com.avenue.baseframework.restclient.utils.RestOptions
import com.github.florent37.viewanimator.ViewAnimator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseActivity() {

    private lateinit var binding: ActivitySplashBinding

    @Inject
    lateinit var userSettings: UserSettings

    @Inject
    lateinit var restConnector: RestConnector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initApplication()
    }

    private fun initApplication() {
        val imageView: ImageView = findViewById(R.id.imageViewSplash)
        ViewAnimator.animate(imageView).alpha(imageView.alpha, 0f).duration(2000)
            .onStop { this.checkShowNextActivity() }
            .start()
    }

    private fun checkShowNextActivity() {
        val host: String = userSettings.getServerHost()!!
        val port: String = userSettings.getServerPort()!!
        val webPort: String = userSettings.getServerWebPort()!!
        if (host == EString.EMPTY || port == EString.EMPTY) {
            startActivity(NetworkActivity::class.java)
        } else {
            val username: String = userSettings.getUserName()!!
            val password: String = userSettings.getPassword()!!
            val restOptions = RestOptions(host, port.toInt())
            restConnector.restOptions = restOptions
            if (username == EString.EMPTY || password == EString.EMPTY) {
                startActivity(LoginActivity::class.java)
            } else {
                restOptions.userName = username
                restOptions.password = password
                startActivity(MainActivity2::class.java)
            }
        }
    }
}