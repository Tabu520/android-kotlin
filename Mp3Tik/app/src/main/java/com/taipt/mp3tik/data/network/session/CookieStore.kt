package com.taipt.mp3tik.data.network.session

interface CookieStore {

    var cookie: String?

    fun clear()
}