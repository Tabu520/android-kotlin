package com.avenue.baseframework.restclient.utils

import java.io.Serializable

interface RestEntity: Serializable {

    fun canSaveToServer(): Boolean

    @Throws(RestException::class)
    fun validateForServer()

    fun close()
}