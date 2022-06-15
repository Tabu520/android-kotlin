package com.taipt.rxjavabasic

import android.util.Log
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import java.util.*

val mListNum = mutableListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
val mArrayNum = arrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

fun justOperator() {
    val observable = Observable.just(mListNum)

    val observer = object : Observer<List<Int>> {
        override fun onSubscribe(d: Disposable) {
            Log.d(MainActivity.TAG, "onSubscribe: ${d.isDisposed}")
        }

        override fun onError(e: Throwable) {
            Log.d(MainActivity.TAG, "onError: ${e.message}")
        }

        override fun onComplete() {
            Log.d(MainActivity.TAG, "onComplete")
        }

        override fun onNext(t: List<Int>) {
            Log.d(MainActivity.TAG, "onNext: $t")
        }


    }

    observable.subscribe(observer)
}

fun fromOperator() {
    val observable = Observable.fromArray(mArrayNum)

    val observer = object : Observer<Array<Int>> {
        override fun onSubscribe(d: Disposable) {
            Log.d(MainActivity.TAG, "onSubscribe: ${d.isDisposed}")
        }

        override fun onError(e: Throwable) {
            Log.d(MainActivity.TAG, "onError: ${e.message}")
        }

        override fun onComplete() {
            Log.d(MainActivity.TAG, "onComplete")
        }

        override fun onNext(t: Array<Int>) {
            Log.d(MainActivity.TAG, "onNext: ${t.contentToString()}")
        }

    }

    observable.subscribe(observer)
}

fun fromIterableOperator() {
    val observable = Observable.fromIterable(mListNum)

    val observer = object : Observer<Int> {
        override fun onSubscribe(d: Disposable) {
            Log.d(MainActivity.TAG, "onSubscribe: ${d.isDisposed}")
        }

        override fun onError(e: Throwable) {
            Log.d(MainActivity.TAG, "onError: ${e.message}")
        }

        override fun onComplete() {
            Log.d(MainActivity.TAG, "onComplete")
        }

        override fun onNext(t: Int) {
            Log.d(MainActivity.TAG, "onNext: $t")
        }

    }

    observable.subscribe(observer)
}