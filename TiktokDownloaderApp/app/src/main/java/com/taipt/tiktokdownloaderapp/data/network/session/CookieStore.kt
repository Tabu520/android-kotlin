package com.taipt.extractaudio.network.session

interface CookieStore {

    var cookie: String?

    fun clear()
}