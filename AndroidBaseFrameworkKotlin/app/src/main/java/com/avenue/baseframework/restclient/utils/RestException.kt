package com.avenue.baseframework.restclient.utils

import java.io.IOException
import java.io.InputStream
import java.util.regex.Pattern

class RestException : Exception {

    private var errorType = 3000
    private var errorCode = 400
    private var maxId: String? = null

    constructor(t: Throwable) : super(t)

    constructor(message: String) : super(message)

    constructor(errorCode: Int, message: String) : super(message) {
        this.errorCode = errorCode
    }

    constructor(message: String, t: Throwable) : super(message, t)

    constructor(errorCode: Int, message: String, t: Throwable) : super(message, t) {
        this.errorCode = errorCode
    }

    companion object {
        val INFO = 1000
        val WARNING = 2000
        val ERROR = 3000

        @Throws(RestException::class)
        fun throwTimeOutException() {
            throw RestException("Cannot connect to Maximo server")
        }

        @Throws(RestException::class)
        fun throwExceptionFromResponseStream(error: Int, inputStream: InputStream) {
            val inputString: String = try {
                RestUtils.getStringFromInputStream(inputStream)
            } catch (var9: IOException) {
                var9.message.toString()
            }
            val fullMsg = inputString.replace(".*:".toRegex(), "").trim { it <= ' ' }
            val exception = RestException(error, fullMsg)
            val p = Pattern.compile("BMX.*-")
            val m = p.matcher(fullMsg)
            if (m.find()) {
                val maxId = m.group(0).split("-").toTypedArray()[0].trim { it <= ' ' }
                exception.maxId = maxId
                val errorType = maxId.substring(maxId.length - 1)
                when(errorType) {
                    "I" -> exception.errorType = INFO
                    "W" -> exception.errorType = WARNING
                    else -> exception.errorType = ERROR
                }
            }
            throw exception
        }
    }

}