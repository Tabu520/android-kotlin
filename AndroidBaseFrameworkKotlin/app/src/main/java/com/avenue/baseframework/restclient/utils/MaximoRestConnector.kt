package com.avenue.baseframework.restclient.utils

import com.google.gson.Gson
import org.json.JSONObject
import java.io.*
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.*

class MaximoRestConnector : Serializable, Cloneable, RestEntity {

    var restOptions: RestOptions? = null
    var initialRestParams: RestParams? = null
        get() {
            return if (field != null) field!!.clone() else null
        }
    var restParams: RestParams? = null
    var webRequest: HttpURLConnection? = null
    var valid = false
    var cookies: List<String>? = null
    var httpMethod: String? = null
    var lastResponseCode = 0
    var connectTimeout = 0
    var readTimeout = 0

    constructor() {
        valid = true
        cookies = null
        httpMethod = "GET"
        lastResponseCode = -1
        connectTimeout = 15000
        readTimeout = 60000
    }

    constructor(restOptions: RestOptions, restParams: RestParams) : this() {
        this.setRestOptions(restOptions)
        this.setRestParams(restParams)
        initialRestParams = this.restParams!!.clone()
    }

    fun getDefaultRestParams(): RestParams {
        return RestParams().put("_format", "json").put("_compact", false).put("_md", true)
            .put("_urs", true).put("_locale", true).put("_tc", true)
    }

    fun setRestOptions(ops: RestOptions?): MaximoRestConnector {
        restOptions = ops
        return this
    }

    fun setRestParams(pars: RestParams?): MaximoRestConnector {
        restParams = pars
        if (initialRestParams == null && restParams != null) {
            initialRestParams = restParams!!.clone()
        }
        return this
    }

    fun setConnectTimeOut(timeOut: Int): MaximoRestConnector {
        connectTimeout = timeOut
        return this
    }

    fun setReadTimeOut(timeOut: Int): MaximoRestConnector {
        readTimeout = timeOut
        return this
    }

    fun resetToInitialParams(): MaximoRestConnector {
        setRestParams(this.initialRestParams)
        return this
    }

    fun clearInitialParams(): MaximoRestConnector {
        initialRestParams = null
        return this
    }

    fun getConnectorURI(requester: RestRequester): String? {
        restOptions?.let {
            it.handlerContext = requester.getHandlerContext()
            return it.getHandlerURI()
        }
        return null
    }

    fun isGET(): Boolean {
        return httpMethod == "GET"
    }

    fun isPOST(): Boolean {
        return httpMethod == "POST"
    }

    fun isPUT(): Boolean {
        return httpMethod == "PUT"
    }

    fun isDELETE(): Boolean {
        return httpMethod == "DELETE"
    }

    fun isValid(): Boolean {
        return valid
    }

    fun isLean(): Boolean {
        return restOptions!!.lean
    }

    fun clearCookies() {
        cookies = null
    }

    @Throws(IOException::class, RestException::class)
    private fun openWebRequest(uri: String, method: String, body: String?) {
        validateForServer()
        try {
            this.createWebRequest(uri)
            this.setMethod(method)
            this.setCookiesForSession()
            if (body != null && body.isNotEmpty()) {
                this.setRequestStream(body)
            }
            println(method.uppercase(Locale.getDefault()) + ": " + uri)
            this.lastResponseCode = webRequest!!.responseCode
            if (this.lastResponseCode >= 400) {
                RestException.throwExceptionFromResponseStream(
                    this.lastResponseCode,
                    webRequest!!.errorStream
                )
            }
            println("Response from connection: " + this.lastResponseCode)
        } catch (var5: SocketTimeoutException) {
            RestException.throwTimeOutException()
        } catch (var5: ConnectException) {
            RestException.throwTimeOutException()
        }
    }

    @Throws(IOException::class, RestException::class)
    private fun doHttpRequest(uri: String, method: String, body: String?): InputStream? {
        if (cookies == null) {
            this.connect()
            lastResponseCode = -1
        }
        openWebRequest(uri, method, body)
        return webRequest!!.inputStream
    }

    fun closeConnection() {
        if (webRequest != null) {
            webRequest!!.disconnect()
            webRequest = null
        }
    }

    @Throws(IOException::class, RestException::class)
    fun connect() {
        validateForServer()
        val connectURI: String = this.restOptions!!.getRootApiURI()
        clearCookies()
        println("Authenticating with Maximo server at $connectURI")
        this.createAuthRequest(connectURI)
        try {
            if (!restOptions!!.isFormAuth()) {
                this.setMethod("GET")
            }
            this.lastResponseCode = webRequest!!.responseCode
            val responseCode: Int = this.lastResponseCode
            if (responseCode == -1) {
                throw RestException("Invalid Request")
            }
            if (responseCode < 400) {
                cookies = webRequest!!.headerFields["Set-Cookie"]
            }
            if (cookies == null) {
                var inStream = webRequest!!.errorStream
                if (inStream == null) {
                    inStream = webRequest!!.inputStream
                }
                RestException.throwExceptionFromResponseStream(
                    this.lastResponseCode,
                    inStream!!
                )
            }
        } catch (var8: SocketTimeoutException) {
            RestException.throwTimeOutException()
        } catch (var8: ConnectException) {
            RestException.throwTimeOutException()
        } finally {
            closeConnection()
        }
    }

    @Throws(IOException::class)
    fun disconnect() {
        println("&&&&&&&&&&&&&&&&&&&& LOGOUT &&&&&&&&&&&&&&&&&&&&")
        val logout: String = restOptions!!.getRootApiURI() + "/logout"
        val httpURL = URL(logout)
        webRequest = httpURL.openConnection() as HttpURLConnection
        try {
            webRequest!!.requestMethod = "GET"
            this.lastResponseCode = webRequest!!.responseCode
        } finally {
            closeConnection()
        }
    }

    @Synchronized
    @Throws(IOException::class, RestException::class)
    fun getJson(uri: String?): JSONObject? {
        return getJson(uri!!, null)
    }

    @Synchronized
    @Throws(IOException::class, RestException::class)
    fun getJson(uri: String, headers: Map<String, Any>?): JSONObject? {
        var uri = uri
        if (!isValid()) {
            throw RestException("The instance of MaximoConnector is not valid.")
        }
        var publicHost: String = restOptions!!.host
        if (restOptions!!.port != -1) {
            publicHost += ":" + restOptions!!.port
        }
        if (!uri.contains(publicHost)) {
            val tempURL = URL(uri)
            var currentHost = tempURL.host
            if (tempURL.port != -1) {
                currentHost += ":" + tempURL.port
            }
            uri = uri.replace(currentHost!!, publicHost)
        }
        val httpURL = URL(uri)
        this.setMethod("GET")
        headers?.let {
            if (it.isNotEmpty()) {
                this.setHeaders(it)
            }
        }
        if (cookies == null) connect()
        this.setCookiesForSession()
        val resCode = webRequest!!.responseCode
        lastResponseCode = resCode
        val inStream: InputStream = if (resCode >= 400) {
            webRequest!!.errorStream
        } else {
            webRequest!!.inputStream
        }
        val reader: Reader = InputStreamReader(inStream, StandardCharsets.UTF_8)
        return Gson().fromJson(reader, JSONObject::class.java)
    }

    @Throws(IOException::class, RestException::class)
    protected fun createWebRequest(uri: String) {
        var uri = uri
        var publicHost: String = restOptions!!.host
        val publicPort: Int = restOptions!!.port
        if (publicPort != -1) {
            publicHost = "$publicHost:$publicPort"
        }
        if (!uri.contains(publicHost)) {
            val tempURL = URL(uri)
            var currentHost = tempURL.host
            if (tempURL.port != -1) {
                currentHost = currentHost + ":" + tempURL.port
            }
            uri = uri.replace(currentHost, publicHost)
        }
        restParams?.let {
            uri += it.construct()
        }
        webRequest = URL(uri).openConnection() as HttpURLConnection
        webRequest!!.connectTimeout = this.connectTimeout
        webRequest!!.readTimeout = this.readTimeout
    }

    @Throws(IOException::class, RestException::class)
    protected fun createAuthRequest(uri: String) {
        val ops: RestOptions = restOptions!!
        if (restOptions!!.isFormAuth()) {
            createWebRequest("$uri/j_security_check")
            webRequest!!.instanceFollowRedirects = false
            webRequest!!.requestMethod = "POST"
            webRequest!!.setRequestProperty(
                "Accept",
                "text/html,application/xhtml+xml,application/xml"
            )
            webRequest!!.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
            webRequest!!.setRequestProperty("Connection", "keep-alive")
            webRequest!!.setRequestProperty("x-public-uri", ops.getHandlerURI())
            webRequest!!.doOutput = true
            val outputStream = webRequest!!.outputStream
            val content = "j_username=" + ops.userName +
                    "&j_password=" + ops.password
            outputStream.write(content.toByteArray())
            outputStream.close()
        } else {
            createWebRequest("$uri/login")
            val encodedUserPwd =
                RestUtils.encodeBase64(ops.userName + ":" + ops.password)
            if (ops.isBasicAuth()) {
                webRequest!!.setRequestProperty("Authorization", "Basic $encodedUserPwd")
            } else if (ops.isMaxAuth()) {
                webRequest!!.setRequestProperty("maxauth", encodedUserPwd)
            }
        }
    }

    protected fun setHeaders(headers: Map<String, Any>) {
        val set = headers.entries
        for ((key, value) in set) {
            webRequest!!.setRequestProperty(key, value.toString())
        }
    }

    @Throws(IOException::class)
    protected fun setMethod(method: String?) {
        httpMethod = method
        val opts: RestOptions = restOptions!!
        webRequest?.let {
            when {
                isGET() -> {
                    it.requestMethod = "GET"
                    it.setRequestProperty("Accept", "application/json")
                    it.useCaches = false
                    it.allowUserInteraction = false
                    it.setRequestProperty("x-public-uri", opts.getHandlerURI())
                }
                isPOST() -> {
                    it.requestMethod = "POST"
                    it.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
                    it.doOutput = true
                    it.setRequestProperty("x-public-uri", opts.getHandlerURI())
                }
                isPUT() -> {
                    it.requestMethod = "PUT"
                    it.setRequestProperty("Type", "application/json")
                    it.doOutput = true
                    it.setRequestProperty("x-public-uri", opts.getHandlerURI())
                }
                isDELETE() -> {
                    it.requestMethod = "DELETE"
                    it.setRequestProperty("Accept", "application/json")
                    it.useCaches = false
                    it.allowUserInteraction = false
                    it.setRequestProperty("x-public-uri", opts.getHandlerURI())
                }
            }
        }
    }

    @Throws(IOException::class)
    protected fun setRequestStream(content: String?) {
        val writer = OutputStreamWriter(webRequest!!.outputStream, StandardCharsets.UTF_8)
        writer.write(content)
        writer.flush()
        writer.close()
    }

    private fun setCookiesForSession() {
        if (cookies != null) {
            val var1: Iterator<*> = cookies!!.iterator()
            while (var1.hasNext()) {
                val cookie = var1.next() as String
                webRequest!!.setRequestProperty(
                    "Cookie",
                    cookie.split(";", ignoreCase = true, limit = 0).first()
                )
            }
        }
    }

    @Synchronized
    @Throws(IOException::class, RestException::class)
    fun getFile(uri: String?): ByteArray {
        openWebRequest(uri!!, "GET", null)
        return RestUtils.getBytesFromInputStream(webRequest!!.inputStream)
    }

    @Synchronized
    @Throws(IOException::class, RestException::class)
    operator fun get(uri: String?, requester: RestRequester) {
        requester.handleResponse(doHttpRequest(uri!!, "GET", null))
    }

    @Synchronized
    @Throws(IOException::class, RestException::class)
    fun put(uri: String?, requester: RestRequester) {
        requester.handleResponse(doHttpRequest(uri!!, "PUT", null))
    }

    @Synchronized
    @Throws(IOException::class, RestException::class)
    fun post(uri: String?, body: String?, requester: RestRequester) {
        requester.handleResponse(doHttpRequest(uri!!, "POST", body!!))
    }

    @Synchronized
    @Throws(IOException::class, RestException::class)
    fun delete(uri: String?) {
        doHttpRequest(uri!!, "DELETE", null)
    }

    @Throws(CloneNotSupportedException::class)
    override fun clone(): Any {
        return super.clone()
    }

    override fun canSaveToServer(): Boolean {
        return true
    }

    override fun validateForServer() {
        if (!isValid()) {
            throw RestException("The instance of MaximoRestConnector is not valid.")
        }
    }

    override fun close() {
        restOptions = null
        initialRestParams = null
        restParams = null
        valid = false
    }
}