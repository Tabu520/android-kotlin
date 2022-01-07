package com.avenue.baseframework.core.extensions

import androidx.annotation.GuardedBy
import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.HttpHeaderParser

class InputStreamRequest(
    method: Int,
    url: String,
    var listener: Response.Listener<ByteArray>,
    errorListener: Response.ErrorListener?
) : Request<ByteArray>(method, url, errorListener) {

    //create a static map for directly accessing headers
    var responseHeaders: Map<String, String>? = mapOf()

    init {
        setShouldCache(false)
    }

    override fun parseNetworkResponse(response: NetworkResponse?): Response<ByteArray> {
        responseHeaders = response!!.headers
        return Response.success(response.data, HttpHeaderParser.parseCacheHeaders(response))
    }

    override fun deliverResponse(response: ByteArray?) {
        listener.onResponse(response)
    }
}