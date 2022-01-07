package com.avenue.baseframework.restclient.utils

import com.avenue.baseframework.restclient.utils.RestException
import java.io.InputStream

interface RestRequester {

    @Throws(RestException::class)
    fun loadFromServer()

    @Throws(RestException::class)
    fun reloadFromServer()

    @Throws(RestException::class)
    fun saveToServer()

    @Throws(RestException::class)
    fun handleResponse(response: InputStream?)

    @Throws(RestException::class)
    fun disconnect()

    fun getHandlerContext(): String?

    fun getURI(): String?
}