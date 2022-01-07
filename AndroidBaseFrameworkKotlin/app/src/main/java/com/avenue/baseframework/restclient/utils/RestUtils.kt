package com.avenue.baseframework.restclient.utils

import android.os.Build
import java.io.*
import java.net.URLEncoder
import java.nio.charset.Charset
import java.util.*
import javax.xml.datatype.DatatypeConfigurationException
import javax.xml.datatype.DatatypeFactory

object RestUtils {

    @Throws(UnsupportedEncodingException::class)
    fun encodeBase64(input: String): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Base64.getEncoder().encodeToString(input.toByteArray(Charset.defaultCharset()))
        } else {
            android.util.Base64.encodeToString(input.toByteArray(), android.util.Base64.DEFAULT)
        }
    }

    @Throws(UnsupportedEncodingException::class)
    fun decodeBase64(base64String: String?): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String(Base64.getDecoder().decode(base64String))
        } else {
            String(android.util.Base64.decode(base64String, android.util.Base64.DEFAULT))
        }
    }

    @Throws(UnsupportedEncodingException::class)
    fun encodeByteArrayToString(byteArray: ByteArray): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Base64.getEncoder().encodeToString(byteArray)
        } else {
            android.util.Base64.encodeToString(byteArray, android.util.Base64.DEFAULT)
        }
    }

    @Throws(DatatypeConfigurationException::class, UnsupportedEncodingException::class)
    fun stringValue(o: Any): String {
        return when(o) {
            is Date -> {
                val timeZone = TimeZone.getDefault()
                val gc = GregorianCalendar.getInstance(timeZone) as GregorianCalendar
                gc.time = o
                "\"" + DatatypeFactory.newInstance().newXMLGregorianCalendar(gc).toXMLFormat() + "\""
            }
            is Number -> {
                "" + o
            }
            is Boolean -> {
                o.toString()
            }
            else -> {
                o as String
            }
        }
    }

    @Throws(UnsupportedEncodingException::class)
    fun urlEncode(value: String?): String? {
        return URLEncoder.encode(value, "utf-8")
    }

    @Throws(IOException::class)
    fun getStringFromInputStream(inputStream: InputStream?): String {
        inputStream?.let {
            val reader = BufferedReader(it.reader())
            var content: String
            reader.use {
                content = reader.readText()
            }
            return content
        }
        return ""
    }

    @Throws(IOException::class)
    fun getBytesFromInputStream(inputStream: InputStream?): ByteArray {
        inputStream?.let {
            return it.readBytes()
        }
        return byteArrayOf()
    }

    fun getBooleanAsString(value: Boolean): String {
        return value.toString()
    }

    fun getBooleanAsInteger(value: Boolean): Int {
        return if (value) 1 else 0
    }
}