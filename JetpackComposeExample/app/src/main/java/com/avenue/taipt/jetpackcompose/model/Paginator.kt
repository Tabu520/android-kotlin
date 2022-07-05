package com.avenue.taipt.jetpackcompose.model

interface Paginator<Key, Item> {
    suspend fun loadNextItems()
    fun reset()
}