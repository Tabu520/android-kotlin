package com.avenue.baseframework.restclient.utils

import com.avenue.baseframework.restclient.utils.RestException

interface RestResponseParser {

    @Throws(RestException::class)
    fun parseJsonResponse(parser: Any?)

    @Throws(RestException::class)
    fun parseXMLResponse(parser: Any?)
}