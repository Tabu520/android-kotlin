package taipt4.kotlin.activityresultpermission

import android.content.Context
import android.util.Log
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

class RequestPermissions {
    companion object {
        fun requestPermissions(
            context: Context,
            callback: RequestPermissionCallback,
            permissions: ArrayList<String>
        ) {
            Log.d("TaiPT", "requestPermissions ---- ")
            Dexter.withContext(context).withPermissions(permissions)
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                        Log.d("TaiPT", "onPermissionsChecked ---- ")
                        if (p0!!.areAllPermissionsGranted()) {
                            callback.onGranted()
                        } else if (p0.isAnyPermissionPermanentlyDenied) {
                            callback.onDenied()
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        p0: MutableList<PermissionRequest>?,
                        p1: PermissionToken?
                    ) {

                    }

                }).check()
        }
    }

}