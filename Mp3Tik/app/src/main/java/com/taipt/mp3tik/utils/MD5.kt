package com.taipt.mp3tik.utils

import java.security.MessageDigest

object MD5 {

    private val hexDigits = charArrayOf(
        48.toChar(),
        49.toChar(),
        50.toChar(),
        51.toChar(),
        52.toChar(),
        53.toChar(),
        54.toChar(),
        55.toChar(),
        56.toChar(),
        57.toChar(),
        97.toChar(),
        98.toChar(),
        99.toChar(),
        100.toChar(),
        101.toChar(),
        102.toChar()
    )

    fun hexdigest(paramString: String): String? {
        try {
            return hexdigest(paramString.toByteArray())
        } catch (localException: Exception) {
        }
        return null
    }

    fun hexdigest(paramArrayOfByte: ByteArray?): String? {
        try {
            val localMessageDigest = MessageDigest.getInstance("MD5")
            localMessageDigest.update(paramArrayOfByte)
            val arrayOfByte = localMessageDigest.digest()
            val arrayOfChar = CharArray(32)
            var i = 0
            var j = 0
            while (true) {
                if (i >= 16) return String(arrayOfChar)
                val k = arrayOfByte[i].toInt()
                val m = j + 1
                arrayOfChar[j] = hexDigits[0xF and k ushr 4]
                j = m + 1
                arrayOfChar[m] = hexDigits[k and 0xF]
                i++
            }
        } catch (localException: Exception) {
        }
        return null
    }
}