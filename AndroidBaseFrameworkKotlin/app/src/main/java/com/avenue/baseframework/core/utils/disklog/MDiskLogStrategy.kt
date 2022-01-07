package com.avenue.baseframework.core.utils.disklog

import android.os.Environment
import com.avenue.baseframework.core.BaseApplication
import com.orhanobut.logger.Logger
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

object MDiskLogStrategy {

    // TODO("Check file path")
    fun getLogFile(): File? {
        Logger.d("MDiskLogStrategy : getLogFile")
        val diskPath = BaseApplication.getContext().getExternalFilesDir(null)!!.absolutePath
        val folderName = diskPath + File.separatorChar + "logger"
        val fileName = "logs"
        val copyFileName = "logs_eLB"
        val folder = File(folderName)
        if (!folder.exists()) {
            folder.mkdirs()
        }
        var newFileCount = 0
        var newFile: File
        var existingFile: File? = null
        newFile = File(folder, String.format("%s_%s.csv", fileName, newFileCount))
        while (newFile.exists()) {
            existingFile = newFile
            newFileCount++
            newFile = File(folder, String.format("%s_%s.csv", fileName, newFileCount))
        }
        if (existingFile != null) {
            return copyLoggerFile(folder, existingFile, copyFileName, newFileCount)
            //return existingFile;
        }
        copyLoggerFile(folder, newFile, copyFileName, newFileCount)
        return newFile
    }

    private fun copyLoggerFile(
        folder: File,
        orgFile: File,
        fileName: String,
        fileCount: Int
    ): File? {
        val backupFile = File(folder, String.format("%s_%s.csv", fileName, fileCount))
        try {
            if (orgFile.exists() && folder.canWrite()) {
                val src = FileInputStream(orgFile).channel
                val dst = FileOutputStream(backupFile).channel
                dst.transferFrom(src, 0, src.size())
                src.close()
                dst.close()

                //orgFile.delete();
                return backupFile
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    fun getLogCatFile(): File {
        var fileLogCat = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "logcat_eLB.txt"
        )
        if (fileLogCat.exists()) {
            val url_LogCat = fileLogCat.absolutePath
            fileLogCat.delete()
            fileLogCat = File(url_LogCat)
        }
        try {
            Runtime.getRuntime().exec("logcat -f " + fileLogCat.absolutePath)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return fileLogCat
    }
}