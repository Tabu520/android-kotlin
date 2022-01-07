package com.avenue.baseframework.core.extensions

import androidx.annotation.GuardedBy
import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.HttpHeaderParser
import java.nio.charset.StandardCharsets

class StringUTF8Request(
    method: Int,
    url: String,
    listener: Response.Listener<String>,
    errorListener: Response.ErrorListener?
) : Request<String>(method, url, errorListener) {

    /** Lock to guard mListener as it is cleared on cancel() and read on delivery.  */
    private val mLock = Any()

    @GuardedBy("mLock")
    private var mListener: Response.Listener<String>? = null

    init {
        mListener = listener
    }

    constructor(
        url: String,
        listener: Response.Listener<String>,
        errorListener: Response.ErrorListener?
    ) : this(Method.GET, url, listener, errorListener)


    override fun parseNetworkResponse(response: NetworkResponse?): Response<String> {
        val parsed = String(response!!.data, StandardCharsets.UTF_8)
        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response))
    }

    override fun deliverResponse(response: String?) {
        var listener: Response.Listener<String>?
        synchronized(mLock) { listener = mListener }
        if (listener != null) {
            listener!!.onResponse(response)
        }
    }

    override fun cancel() {
        super.cancel()
        synchronized(mLock) { mListener = null }
    }
}