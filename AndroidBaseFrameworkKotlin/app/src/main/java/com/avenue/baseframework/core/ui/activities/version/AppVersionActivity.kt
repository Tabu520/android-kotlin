package com.avenue.baseframework.core.ui.activities.version

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import com.avenue.baseframework.R
import com.avenue.baseframework.core.ui.activities.BaseActivity
import com.avenue.baseframework.core.utils.NetUtils
import com.avenue.baseframework.restclient.RestConnector
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class AppVersionActivity: BaseActivity() {

    @Inject
    lateinit var restConnector: RestConnector

    private var dialogInit: AlertDialog? = null
    private var dialogUpdateVersion: Dialog? = null
    private var dialogCheckNetwork: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dialogUpdateVersion = createDialog("Vui lòng cập nhật version mới.", "Đồng ý")
        dialogUpdateVersion!!.setCanceledOnTouchOutside(false)
        dialogUpdateVersion!!.setCancelable(false)
        dialogUpdateVersion!!.show()
    }

    override fun onDialogPositiveClick(dialog: DialogInterface?) {
        if (this.dialogUpdateVersion == dialog || this.dialogCheckNetwork == dialog) {
            downloadApkFile()
        }
    }

    private fun showInitializingDialog(message: String) {
        if (dialogInit == null) {
            val dialogBuilder = AlertDialog.Builder(this)
            //LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            val inflater = this.layoutInflater
            val dialogView: View = inflater.inflate(R.layout.dialog_loading, null)
            dialogBuilder.setView(dialogView)
            val textView = dialogView.findViewById<TextView>(R.id.tvInitialize)
            textView.text = message
            dialogInit = dialogBuilder.create()
            dialogInit!!.setCanceledOnTouchOutside(false)
            dialogInit!!.show()
        }
    }

    private var startInstallApp: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_CANCELED) {
            dialogUpdateVersion!!.show()
        }
    }

    private fun downloadApkFile() {
        if (dialogInit == null) {
            showInitializingDialog("Đang tải file mới...")
        }
        val fileName = "elogbook-kvt-release.apk"
        restConnector.downloadApkFile(fileName) { response ->
            //Log.d(TAG, "update apk: " + response);
            if (response == null) {
                val savePath =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                var apkURI = FileProvider.getUriForFile(
                    this,
                    applicationContext.packageName.toString() + ".provider",
                    File(savePath, fileName)
                )
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    val install = Intent(Intent.ACTION_INSTALL_PACKAGE)
                    install.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    install.data = apkURI
                    install.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                    dialogInit?.dismiss()
                    //startActivity(install);
                    startInstallApp.launch(install)
                } else {
                    apkURI = Uri.fromFile(File(savePath, fileName))
                    val install = Intent(Intent.ACTION_VIEW)
                    install.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    install.setDataAndType(apkURI, "application/vnd.android.package-archive")
                    dialogInit?.dismiss()
                    startInstallApp.launch(install)
                }
            } else {
                dialogInit?.dismiss()
                if (!NetUtils.isNetworkAvailable()) {
                    dialogCheckNetwork =
                        createDialog(getString(R.string.error_no_network), getString(R.string.ok))
                    dialogCheckNetwork!!.setCancelable(false)
                    dialogCheckNetwork!!.setCanceledOnTouchOutside(false)
                    dialogCheckNetwork!!.show()
                } else {
                    createDialog(getString(R.string.error_update), getString(R.string.ok))!!.show()
                }
            }
        }
    }
}