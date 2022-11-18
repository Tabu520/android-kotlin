package com.taipt.tiktokdownloaderapp.data.network.session

interface CookieStore {

    var cookie: String?

    fun clear()
}