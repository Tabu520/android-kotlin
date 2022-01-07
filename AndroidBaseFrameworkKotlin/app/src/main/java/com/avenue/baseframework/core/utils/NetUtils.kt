package com.avenue.baseframework.core.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.android.volley.VolleyError
import com.avenue.baseframework.core.BaseApplication
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.io.IOException
import java.net.InetAddress
import java.net.NetworkInterface
import java.nio.charset.StandardCharsets
import java.util.*
import kotlin.experimental.and

object NetUtils {

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val nw = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            //for other device how are able to connect with Ethernet
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            //for check internet over Bluetooth
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
            else -> false
        }
    }

    fun getErrorMessage(error: VolleyError): String {
        val responseBody: String = try {
            if (error.networkResponse.data != null) {
                String(error.networkResponse.data, StandardCharsets.UTF_8)
            } else {
                "Unknown error, please contact admin!"
            }
        } catch (ex: Exception) {
            "getErrorMessage: $ex"
        }
        return responseBody
    }

    fun bytesToHex(bytes: ByteArray): String {
        val sbuf = StringBuilder()
        for (aByte in bytes) {
            val intVal: Int = (aByte and 0xff.toByte()).toInt()
            if (intVal < 0x10) sbuf.append("0")
            sbuf.append(Integer.toHexString(intVal).uppercase(Locale.getDefault()))
        }
        return sbuf.toString()
    }

    fun getUTF8Bytes(str: String): ByteArray? {
        return try {
            str.toByteArray(StandardCharsets.UTF_8)
        } catch (ex: Exception) {
            null
        }
    }

    @Throws(IOException::class)
    fun loadFileAsString(filename: String?): String {
        val BUFLEN = 1024
        BufferedInputStream(FileInputStream(filename), BUFLEN).use { bis ->
            val baos = ByteArrayOutputStream(BUFLEN)
            val bytes = ByteArray(BUFLEN)
            var isUTF8 = false
            var read: Int
            var count = 0
            while (bis.read(bytes).also { read = it } != -1) {
                if (count == 0 && bytes[0] == 0xEF.toByte() && bytes[1] == 0xBB.toByte() && bytes[2] == 0xBF.toByte()
                ) {
                    isUTF8 = true
                    baos.write(bytes, 3, read - 3) // drop UTF8 bom marker
                } else {
                    baos.write(bytes, 0, read)
                }
                count += read
            }
            return if (isUTF8) String(
                baos.toByteArray(),
                StandardCharsets.UTF_8
            ) else baos.toString()
        }
    }

    fun getDeviceID(interfaceName: String?): String {
        try {
            val interfaces: List<NetworkInterface> =
                Collections.list(NetworkInterface.getNetworkInterfaces())
            for (intf in interfaces) {
                if (interfaceName != null) {
                    if (!intf.name.equals(interfaceName, ignoreCase = true)) continue
                }
                val mac = intf.hardwareAddress ?: return ""
                val buf = StringBuilder()
                for (aMac in mac) buf.append(String.format("%02X:", aMac))
                if (buf.isNotEmpty()) buf.deleteCharAt(buf.length - 1)
                return buf.toString()
            }
        } catch (ignored: Exception) {
        } // for now eat exceptions
        return ""
    }

    fun getIPAddress(useIPv4: Boolean): String {
        var ipaddress = ""
        try {
            val interfaces: List<NetworkInterface> =
                Collections.list(NetworkInterface.getNetworkInterfaces())
            for (intf in interfaces) {
                val addrs: List<InetAddress> = Collections.list(intf.inetAddresses)
                for (addr in addrs) {
                    if (!addr.isLoopbackAddress) {
                        val sAddr = addr.hostAddress
                        val isIPv4 = sAddr!!.indexOf(':') < 0
                        if (useIPv4) {
                            if (isIPv4) ipaddress = sAddr
                        } else {
                            if (!isIPv4) {
                                val delim = sAddr.indexOf('%') // drop ip6 zone suffix
                                ipaddress = if (delim < 0) sAddr.uppercase(Locale.getDefault()) else sAddr.substring(
                                    0,
                                    delim
                                ).uppercase(Locale.getDefault())
                            }
                        }
                    }
                    Log.d("IPADDRESS", "ip: $ipaddress")
                }
            }
        } catch (ignored: Exception) {
        } // for now eat exceptions
        return ipaddress
    }


    private fun formatIP(ip: Int): String {
        return String.format(
            "%d.%d.%d.%d",
            ip and 0xff,
            ip shr 8 and 0xff,
            ip shr 16 and 0xff,
            ip shr 24 and 0xff
        )
    }

    private fun getBytesIP(ip: Int): ByteArray {
        val ba = (ip and 0xff).toByte()
        val bb = (ip shr 8 and 0xff).toByte()
        val bc = (ip shr 16 and 0xff).toByte()
        val bd = (ip shr 24 and 0xff).toByte()
        return byteArrayOf(ba, bb, bc, bd)
    }
}