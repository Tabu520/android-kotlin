package com.avenue.baseframework.restclient.utils

import android.util.Log
import com.avenue.baseframework.restclient.utils.RestConstants.AUTH_BASIC
import com.avenue.baseframework.restclient.utils.RestConstants.AUTH_FORM
import com.avenue.baseframework.restclient.utils.RestConstants.AUTH_MAXAUTH
import com.avenue.baseframework.restclient.utils.RestConstants.AUTH_TOKEN
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.net.URL

class RestOptions {

    val TAG = "RestOptions"
    var host = "http://192.168.1.168"
    var port = 80
    var authMode: String = AUTH_TOKEN
    var userName = ""
    var password = ""
    var ssl = false
    var multiTenancy = false
    var lean = false
    var publicURI = ""
    var restContext = "maxrest/rest"
    var oslcContext = "maxrest/oslc"
    var restQuery = ""
    var serverKey = ""
    var token = ""
    var restParams: RestParams? = null
    var handlerContext: String? = null
    var appURI: String? = null
    var tenantCode = "00"

    companion object {
        fun getDefault(): RestOptions {
            return RestOptions()
        }
    }

    constructor()

    constructor(host: String, port: Int) : this() {
        this.host = host
        this.port = port
    }

    constructor(host: String, port: Int, authMode: String) : this() {
        this.host = host
        this.port = port
        this.authMode = authMode
    }

    constructor(host: String, port: Int, authMode: String, restContext: String) : this() {
        this.host = host
        this.port = port
        this.authMode = authMode
        this.restContext = restContext
    }

    fun getMaxAuthValue(): String {
        var maxAuth = ""
        try {
            maxAuth = RestUtils.encodeBase64("$userName:$password")
        } catch (ex: UnsupportedEncodingException) {
            Log.e(TAG, "Exception: " + ex.message)
        }
        return maxAuth
    }

    fun setHost(host: String?): RestOptions {
        this.host = host!!
        return this
    }

    fun setPort(port: Int): RestOptions {
        this.port = port
        return this
    }

    fun setAuthMode(authMode: String?): RestOptions {
        this.authMode = authMode!!
        return this
    }

    fun setRestContext(restContext: String?): RestOptions {
        this.restContext = restContext!!
        return this
    }

    fun setRestQuery(restQuery: String?): RestOptions {
        this.restQuery = restQuery!!
        return this
    }

    fun setServerKey(key: String?): RestOptions {
        serverKey = key!!
        return this
    }

    fun setToken(token: String?): RestOptions {
        this.token = token!!
        return this
    }

    fun setParams(params: RestParams?): RestOptions {
        restParams = params
        return this
    }

    fun setHttps(): RestOptions {
        ssl = true
        return this
    }

    fun setHttp(): RestOptions {
        ssl = false
        return this
    }

    fun multiTenancy(mtMode: Boolean): RestOptions {
        this.multiTenancy = mtMode
        return this
    }

    fun appURI(appURI: String?): RestOptions {
        this.appURI = appURI
        return this
    }

    fun lean(lean: Boolean): RestOptions {
        this.lean = lean
        return this
    }

    fun tenantCode(tenantCode: String): RestOptions {
        this.tenantCode = tenantCode
        return this
    }

    fun isBasicAuth(): Boolean {
        return authMode == AUTH_BASIC
    }

    fun isMaxAuth(): Boolean {
        return authMode == AUTH_MAXAUTH
    }

    fun isTokenAuth(): Boolean {
        return authMode == AUTH_TOKEN
    }

    fun isFormAuth(): Boolean {
        return authMode == AUTH_FORM
    }

    fun getURLString(): String? {
        var url =
            (if (ssl) "https://" else "http://") + host + ":" + port + "/" + restContext + "/" + restQuery
        if (restParams != null) {
            url = url + "?" + restParams!!.getParamsString()
        }
        return url
    }

    @Throws(IOException::class)
    fun getUrl(): URL {
        return URL((if (ssl) "https://" else "http://") + host + ":" + port + "/" + restContext + "/" + restQuery)
    }

    fun getURLStringNoParams(): String? {
        return (if (ssl) "https://" else "http://") + host + ":" + port + "/" + restContext + "/" + restQuery
    }

    fun getHandlerURI(): String {
        return getRootApiURI() + "/" + handlerContext
    }

    fun getRootApiURI(): String {
        return (if (ssl) "https://" else "http://") + host + ":" + port + "/" + restContext
    }

    //Get app URI
    fun getAppUri(): String? {
        appURI?.let {
            if (multiTenancy && !appURI!!.contains("tenantcode"))
                appURI += (if (appURI!!.contains("?")) "" else "?") + "&_tenantcode=" + tenantCode
            if (this.lean) {
                if (appURI!!.contains("&lean=0")) {
                    appURI = appURI!!.replace("&lean=0", "&lean=1")
                } else if (!appURI!!.contains("&lean=1")) {
                    appURI += (if (appURI!!.contains("?")) "" else "?") + "&lean=1"
                }
            } else {
                if (appURI!!.contains("&lean=1")) {
                    appURI = appURI!!.replace("&lean=1", "&lean=0")
                } else if (!appURI!!.contains("&lean=0")) {
                    appURI += (if (appURI!!.contains("?")) "" else "?") + "&lean=0"
                }
            }
            return appURI
        }
        val strb = StringBuffer(if (ssl) "https://" else "http://")
        strb.append(host)
        strb.append(":").append(port)
        strb.append("/").append(restContext)
        if (multiTenancy) {
            strb.append(if (strb.toString().contains("?")) "" else "?").append("&_tenantcode=")
                .append(tenantCode)
        }
        if (lean) {
            strb.append(if (strb.toString().contains("?")) "" else "?").append("&lean=1")
        }
        appURI = strb.toString()
        return appURI
    }

    //Get public URI
    fun getPublicUri(): String {
        if (publicURI != null) return publicURI else if (appURI != null) {
            val strs = appURI!!.split("/").toTypedArray()[2].split(":").toTypedArray()
            host = strs[0]
            if (strs.size > 1) port = strs[1].toInt()
        }
        val strb = StringBuffer(if (ssl) "https://" else "http://")
        strb.append(host)
        strb.append(":").append(port)
        strb.append("/").append(restContext)
        //if(mt == true) strb.append("?&_tenantcode="+tenantcode);
        publicURI = strb.toString()
        return publicURI
    }

}