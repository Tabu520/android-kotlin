package com.avenue.baseframework.core.helpers

import com.avenue.baseframework.core.BaseApplication
import com.avenue.baseframework.core.db.entity.BaseEntity
import java.io.*
import java.nio.charset.StandardCharsets

object FileFormat {

    private const val TAG = "FileFormat"
    private val diskPath = BaseApplication.getContext().getExternalFilesDir(null)!!.absolutePath
    private val folderName = diskPath + File.separatorChar + "requestFile"

    fun generateCsvFile(sFileName: String?): File {
        val folder = File(folderName)
        if (!folder.exists()) {
            folder.mkdirs()
        }
        var file = File(folderName, sFileName!!)
        if (file.exists()) {
            file.delete()
        }
        file = File(folderName, sFileName)
        return file
    }

    fun appendDataToFile(filename: String?, list: List<String?>) {
        val file = generateCsvFile(filename)
        val writer: FileWriter?
        try {
            writer = FileWriter(file, true)
            for (s in list) {
                writer.append(s)
                writer.append("\n")
            }
            writer.flush()
            writer.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun deleteFile(fileName: String?) {
        val file = File(folderName, fileName!!)
        if (file.exists()) {
            file.delete()
        }
    }

    fun getContent(fileName: String?): String {
        var content = ""
        val file = File(folderName, fileName!!)
        if (file.exists()) {
            try {
                var inputStream: InputStream? = null
                if (file.exists()) {
                    inputStream = FileInputStream(file)
                }
                if (inputStream != null) {
                    val size = inputStream.available()
                    val buffer = ByteArray(size)
                    inputStream.read(buffer)
                    inputStream.close()
                    content = String(buffer, StandardCharsets.UTF_8)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return content
    }

    private fun appendCsvFormat(list: List<String>): String {
        val stringBuilder = StringBuilder()
        if (list.isEmpty()) return EString.EMPTY
        for (s in list) {
            stringBuilder.append(s)
            stringBuilder.append("\n")
        }
        return stringBuilder.toString()
    }

    fun pushEntityToCSVFile(entity: BaseEntity, list: MutableList<String>): String {
        if (list.isNotEmpty()) {
            entity.getInsertCsvHeader()?.let { list.add(0, it) }
        }
        return appendCsvFormat(list)
    }
}