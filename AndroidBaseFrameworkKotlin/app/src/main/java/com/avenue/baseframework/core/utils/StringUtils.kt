package com.avenue.baseframework.core.utils

import android.os.Environment
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.URLSpan
import android.widget.TextView
import com.avenue.baseframework.core.BaseApplication
import com.avenue.baseframework.R
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.nio.charset.StandardCharsets
import java.util.regex.Pattern

object StringUtils {

    private const val URL_DownloadData = "http://116.118.113.24:1111/downloads"
    private const val UnzipFolderDir = "UnzipFolderDir"
    private const val FilesDir = "FilesDir"
    private const val androidVersionFile = "android_version.zip"
    private const val version_txt = "version.txt"

    fun getDownloadDataURL(): String? {
        return URL_DownloadData
    }

    fun getPackageNameStoreDir(): String {
        return String.format(
            "%s/%s",
            Environment.getExternalStorageDirectory(),
            BaseApplication.getContext().resources.getString(R.string.app_name)
        )
    }

    fun getDownloadedFileZipDir(filename: String): String {
        return String.format("%s/%s/", getPackageNameStoreDir(), filename.replace(".zip", ""))
    }

    fun getDownloadedVersionZipDir(): String {
        return String.format(
            "%s/%s",
            getPackageNameStoreDir(),
            androidVersionFile.replace(".zip", "")
        )
    }

    fun getUnzipFolderDir(): String {
        return String.format("%s/%s/", getPackageNameStoreDir(), UnzipFolderDir)
    }

    fun getFilesDir(): String {
        return String.format("%s%s/", getUnzipFolderDir(), FilesDir)
    }

    fun getVersionDownloadURL(): String {
        return String.format("%s/%s", URL_DownloadData, androidVersionFile)
    }

    fun getFileDownloadURL(filename: String?): String {
        return String.format("%s/%s", URL_DownloadData, filename)
    }

    fun getVersionTxtDir(): String {
        return String.format(
            "%s%s/%s",
            getFilesDir(),
            androidVersionFile.replace(".zip", ""),
            version_txt
        )
    }

    fun getJsonAssetsExternalStoragePath(path: String?): String {
        var json = ""
        try {
            path?.let {
                val inputStream: InputStream
                val file = File(it)
                inputStream = if (file.exists()) {
                    FileInputStream(file)
                } else { // get from Assets
                    BaseApplication.getContext().assets.open(it)
                }
                val size = inputStream.available()
                val buffer = ByteArray(size)
                inputStream.read(buffer)
                inputStream.close()
                json = String(buffer, StandardCharsets.UTF_8)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return json
    }

    fun convertByteArrayToHexString(inputArray: ByteArray): String {
        var i: Int
        var input: Int
        val hex =
            arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F")
        val output = StringBuilder()
        var j = 0
        while (j < inputArray.size) {
            input = inputArray[j].toInt() and 0xff
            i = input shr 4 and 0x0f
            output.append(hex[i])
            i = input and 0x0f
            output.append(hex[i])
            ++j
        }
        return output.toString()
    }

    fun stripUnderlines(tv: TextView, text: String?) {
        val s: Spannable = SpannableString(text)
        val spans = s.getSpans(0, s.length, URLSpan::class.java)
        for (span in spans) {
            val start = s.getSpanStart(span)
            val end = s.getSpanEnd(span)
            s.removeSpan(span)
            s.setSpan(URLSpanNoUnderline(span.url), start, end, 0)
        }
        tv.text = s
    }

    fun getString(stringID: Int): String {
        return BaseApplication.getContext().getString(stringID)
    }

    fun isAlphabet(value: String?): Boolean {
        val pattern = Pattern.compile("[a-zA-Z]")
        return pattern.matcher(value!!).find()
    }

    //region Trim
    // String text = "  fo o   ";
    // output: "fo o  "
    fun trimLeadingWhiteSpace(value: String): String {
        return value.replaceFirst("^\\s*".toRegex(), "")
    }

    // output: "  fo o"
    fun trimTrailingWhiteSpace(value: String): String {
        return value.replaceFirst("\\s++$".toRegex(), "")
    }

    // output: "foo"
    fun trimAllWhiteSpace(value: String): String {
        return value.replace("\\s+".toRegex(), "")
    }

    // output: "fo o"
    fun trimLeadingAndTrailingWhiteSpace(value: String): String {
        return value.trim { it <= ' ' }
    }
    //endregion


    class URLSpanNoUnderline(url: String?) : URLSpan(url) {
        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isUnderlineText = false
        }
    }
}