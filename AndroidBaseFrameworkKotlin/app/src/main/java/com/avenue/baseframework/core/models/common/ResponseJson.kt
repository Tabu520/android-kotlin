package com.avenue.baseframework.core.models.common

class ResponseJson<T> {
    var data: List<T>? = null
    var message: String? = null
    var status = 0
}