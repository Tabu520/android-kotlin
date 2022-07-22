package com.taipt.pokedex.utils

sealed class Resouce<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : Resouce<T>(data, null)
    class Error<T>(data: T? = null, message: String) : Resouce<T>(data, message)
//    class Loading<T>(data: T? = null) : Resouce<T>(data)
}
