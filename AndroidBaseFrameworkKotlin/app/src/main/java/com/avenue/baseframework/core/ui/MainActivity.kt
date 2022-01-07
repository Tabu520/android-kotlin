package com.avenue.baseframework.core.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.avenue.baseframework.R
import com.avenue.baseframework.core.db.dao.LoginInfoDao
import com.avenue.baseframework.core.utils.NetUtils
import com.avenue.baseframework.restclient.RestConnector
import com.avenue.baseframework.restclient.utils.RestUtils
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayInputStream
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var loginInfoDao: LoginInfoDao

    @Inject
    lateinit var restConnector: RestConnector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val stream = ByteArrayInputStream("Hello there!".toByteArray())
//        Log.d("TaiPT", String(RestUtils.getBytesFromInputStream(stream)))
        Log.d("TaiPT", loginInfoDao.hashCode().toString())
//        Log.d("TaiPT", RestUtils.decodeBase64(RestUtils.encodeBase64("Tai")))
    }
}