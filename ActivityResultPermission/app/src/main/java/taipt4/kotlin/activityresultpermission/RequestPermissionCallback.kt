package taipt4.kotlin.activityresultpermission

interface RequestPermissionCallback {
    fun onGranted()
    fun onDenied()
}