package com.avenue.baseframework.restclient

import android.content.Context
import android.os.Environment
import android.util.Log
import com.android.volley.*
import com.android.volley.toolbox.*
import com.avenue.baseframework.core.BaseApplication
import com.avenue.baseframework.core.extensions.InputStreamRequest
import com.avenue.baseframework.core.helpers.Constants
import com.avenue.baseframework.core.helpers.EString
import com.avenue.baseframework.core.models.BGMUser
import com.avenue.baseframework.core.models.UserSettings
import com.avenue.baseframework.restclient.utils.RestConstants
import com.avenue.baseframework.restclient.utils.RestOptions
import com.orhanobut.logger.Logger
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.util.HashMap
import javax.inject.Inject

class RestConnector @Inject constructor() {

    val TAG = "RestConnector"

    var TIMEOUT = 8000
    var RETRY = 2

    @Inject
    lateinit var userSettings: UserSettings

    var restOptions: RestOptions? = null
    var requestQueue: RequestQueue? = null
    var cache: Cache? = null
    var network: Network? = null
    var user: BGMUser? = null
        get() {
            field?.let {
                return it
            }
            return BGMUser()
        }

    var showResetPassword = false
    var serverKey: String? = null

    init {
        cache = DiskBasedCache(BaseApplication.getContext().cacheDir, 1024 * 1024)
        network = BasicNetwork(HurlStack())
        requestQueue = RequestQueue(cache, network)
        requestQueue!!.start()
        restOptions = RestOptions.getDefault()
    }

    //GET SET END//
    //HELPER FUNCTIONS//
    fun logout() {
        user!!.token = "reset"
        user!!.emailTemplate = null
        user = null
        userSettings.setUserInfo("user was logout")
    }

    private fun getDefaultHeader(): MutableMap<String, String> {
        val params: MutableMap<String, String> = HashMap()
        params["User-Agent"] = "Avenue JSC"
        params["Accept-Language"] = "en"
        params["Content-Type"] = "application/json"
        params["Accept"] = "application/json"
        return params
    }
    //HELPER FUNCTIONS END//

    //REQUEST TEMPLATE//
    private fun requestJson(
        restOptions: RestOptions,
        params: MutableMap<String, String>,
        listener: Response.Listener<JSONObject>,
        errorListener: Response.ErrorListener?
    ) {
        Log.d(TAG, restOptions.getURLString()!!)
        val request: JsonObjectRequest = object : JsonObjectRequest(
            Method.GET,
            restOptions.getURLString(),
            null,
            listener,
            errorListener
        ) {
            override fun getHeaders(): Map<String, String> {
                /*if (getUser().token != null && !getUser().token.equals(EString.empty)) {
                    params.put("token", getUser().token);
                } else */
                if (restOptions.isMaxAuth()) {
                    params[RestConstants.AUTH_MAXAUTH] = restOptions.getMaxAuthValue()
                }
                Log.d(TAG, "requestJson header: $params")
                return params
            }
        }
        request.retryPolicy = DefaultRetryPolicy(
            TIMEOUT,
            0,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES.toFloat()
        )
        requestQueue!!.add(request)
    }

    private fun requestJsonRetryNone(
        restOptions: RestOptions,
        params: MutableMap<String, String>,
        listener: Response.Listener<JSONObject>,
        errorListener: Response.ErrorListener?
    ) {
        Log.d(TAG, restOptions.getURLString()!!)
        val request: JsonObjectRequest = object : JsonObjectRequest(
            Method.GET,
            restOptions.getURLString(),
            null,
            listener,
            errorListener
        ) {
            override fun getHeaders(): Map<String, String> {
                if (user!!.token != null && !user!!.token.equals(EString.EMPTY)) {
                    params["token"] = user!!.token!!
                } else if (restOptions.isMaxAuth()) {
                    params["maxauth"] = restOptions.getMaxAuthValue()
                }
                return params
            }
        }
        request.retryPolicy = DefaultRetryPolicy(
            TIMEOUT,
            0,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES.toFloat()
        )
        requestQueue!!.add(request)
    }

    private fun requestString(
        restOptions: RestOptions,
        params: MutableMap<String, String>,
        listener: Response.Listener<String>,
        errorListener: Response.ErrorListener?
    ) {
        Log.d(TAG, restOptions.getURLString()!!)
        val request: StringRequest = object :
            StringRequest(Method.GET, restOptions.getURLString(), listener, errorListener) {
            override fun getHeaders(): Map<String, String> {
                /*if (getUser().token != null && !getUser().token.equals(EString.empty)) {
                    params.put("token", getUser().token);
                } else */
                if (restOptions.isMaxAuth()) {
                    params["maxauth"] = restOptions.getMaxAuthValue()
                }
                return params
            }
        }
        request.retryPolicy = DefaultRetryPolicy(
            TIMEOUT,
            0,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES.toFloat()
        )
        requestQueue!!.add(request)
    }
    //REQUEST TEMPLATE END//

    //LOGIN FUNCTIONS//
    fun validateServer(
        restOptions: RestOptions,
        listener: Response.Listener<JSONObject>,
        errorListener: Response.ErrorListener?
    ) {
        restOptions.setRestQuery("login")
        restOptions.setAuthMode(RestConstants.AUTH_NONE)
        requestJson(restOptions, getDefaultHeader(), listener, errorListener)
    }

    fun login(
        restOptions: RestOptions,
        listener: Response.Listener<String>,
        errorListener: Response.ErrorListener?
    ) {
        restOptions.setRestQuery("login")
        restOptions.setAuthMode(RestConstants.AUTH_MAXAUTH)
        requestString(restOptions, getDefaultHeader(), listener, errorListener)
    }

    fun forgotPassword(
        restOptions: RestOptions,
        listener: Response.Listener<JSONObject>,
        errorListener: Response.ErrorListener?
    ) {
        restOptions.setRestQuery("forgotpassword")
        requestJsonRetryNone(restOptions, getDefaultHeader(), listener, errorListener)
    }
    //LOGIN FUNCTIONS END//

    fun requestRaw(
        restOptions: RestOptions,
        listener: Response.Listener<String?>?,
        errorListener: Response.ErrorListener?
    ) {
        restOptions.setRestQuery("requestRaw")
        val url = restOptions.getURLString()
        val resetRequest: StringRequest =
            object : StringRequest(Method.PUT, url, listener, errorListener) {
                override fun getHeaders(): Map<String, String> {
                    val params = getDefaultHeader()
                    if (user!!.token != null && !user!!.token.equals(EString.EMPTY)) {
                        params["token"] = user!!.token!!
                    }
                    return params
                }

                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8"
                }

                override fun getBody(): ByteArray {
                    val body: String = restOptions.restParams!!.getParamsJson().toString()
                    return body.toByteArray()
                }
            }
        Log.d(TAG, resetRequest.url)
        requestQueue!!.add(resetRequest)
    }
    //TRACKING FUNCTIONS END//

    //DOWNLOAD FILE
    fun downloadApkFile(fileName: String?, listener: Response.Listener<String?>) {
        val downloadPath = "http://" + userSettings.getServerHost()
            .toString() + ":" + userSettings.getServerWebPort()
            .toString() + "/App/" + Constants.APK_NAME
        val inputStreamRequest = InputStreamRequest(Request.Method.GET, downloadPath, { response ->
            response?.let {
                val savePath =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val file = File(savePath, fileName!!)
                val outputStream: FileOutputStream
                try {
                    if (file.exists()) {
                        file.delete()
                    }
                    outputStream = FileOutputStream(file)
                    outputStream.write(response)
                    outputStream.close()
                    listener.onResponse(null)
                } catch (ex: Exception) {
                    Log.e(TAG, ex.toString())
                    listener.onResponse("ERROR")
                }
            }
        }) { error ->
            Log.d(TAG, error.toString())
            listener.onResponse("ERROR")
        }
        requestQueue!!.add(inputStreamRequest)
    }

    fun downloadXMLTemplate(url: String?, filename: String, listener: Response.Listener<String?>) {
        val inputStreamRequest = InputStreamRequest(Request.Method.GET, url!!, { response ->
            if (response != null) {
                val directory: File = BaseApplication.getContext().filesDir
                val file = File(directory, filename)
                val outputStream: FileOutputStream?
                try {
                    if (file.exists()) {
                        file.delete()
                    }
                    outputStream = FileOutputStream(file)
                    outputStream.write(response)
                    outputStream.close()
                    listener.onResponse(file.absolutePath)
                    Logger.d("downloadXMLTemplate success: " + filename + " to path " + file.absolutePath)
                } catch (ex: Exception) {
                    Log.e(TAG, ex.toString())
                    listener.onResponse(null)
                    Logger.d("downloadXMLTemplate exception: $filename with $ex")
                }
            }
        }) { error ->
            listener.onResponse(null)
            Logger.d("downloadXMLTemplate exception: $filename error: $error")
        }
        requestQueue!!.add(inputStreamRequest)
    }
    //DOWNLOAD FILE
}