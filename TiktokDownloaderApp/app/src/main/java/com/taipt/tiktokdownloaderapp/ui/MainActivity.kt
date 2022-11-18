package com.taipt.tiktokdownloaderapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.taipt.tiktokdownloaderapp.R
import com.taipt.tiktokdownloaderapp.utils.PermissionsChecking
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        PermissionsChecking.checkStoragePermissions(this)
    }
}