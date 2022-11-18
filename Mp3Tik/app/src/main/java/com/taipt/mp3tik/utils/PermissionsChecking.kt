package com.taipt.mp3tik.utils

import android.Manifest
import android.content.Context
import android.widget.Toast
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener

object PermissionsChecking {

    fun checkStoragePermissions(context: Context) {
        Dexter.withContext(context)
            .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object: PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    Toast.makeText(
                        context,
                        "You have storage permissions now",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    Toast.makeText(
                        context,
                        "You have denied storage permissions to download video",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }

            })
            .check()
    }

}