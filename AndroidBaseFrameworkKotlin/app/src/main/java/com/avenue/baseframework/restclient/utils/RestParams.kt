package com.avenue.baseframework.restclient.utils

import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.lang.Exception
import java.net.URLEncoder

class RestParams {

    var params: MutableMap<String, Any> = hashMapOf()

    companion object {
        fun getDefault(): RestParams {
            return RestParams()
        }
    }


    fun getInsertDefault(): RestParams {
        return RestParams()
    }

    fun put(key: String?, value: Any?): RestParams {
        value?.let {
            var value = it
            if (it is Boolean) {
                value = if (it) 1 else 0
            }
            params[key!!] = value
        }
        return this
    }

    operator fun get(key: String?): Any? {
        return params[key]
    }

    fun getBoolean(key: String?): Boolean {
        val value = this[key]
        return value is Int && value == 1
    }

    fun remove(key: String?): RestParams {
        params.remove(key)
        return this
    }

    fun clear(): RestParams {
        params.clear()
        return this
    }

    fun containsKey(key: String?): Boolean {
        return params.containsKey(key)
    }

    fun containsValue(value: Any?): Boolean {
        return params.containsValue(value)
    }

    fun clone(): RestParams {
        val result = RestParams()
        result.params = params.toMutableMap()
        return result
    }

    @Throws(RestException::class)
    fun construct(): String? {
        return try {
            val retStr = StringBuilder("?")
            val var2: Iterator<*> = params.entries.iterator()
            while (var2.hasNext()) {
                val m: Map.Entry<String, Any> = var2.next() as Map.Entry<String, Any>
                val key = m.key
                if (key != "#restquerywhere") {
                    retStr.append(m.key).append("=")
                    retStr.append(URLEncoder.encode(m.value.toString(), "utf-8"))
                } else {
                    retStr.append(m.value)
                }
                retStr.append("&")
            }
            retStr.substring(0, retStr.length - 1)
        } catch (var5: Exception) {
            throw RestException("Errors while constructing REST parameters")
        }
    }

    fun hasFormatSet(): Boolean {
        return this["_format"] != null
    }

    fun isJSONFormat(): Boolean {
        return hasFormatSet() && this["_format"] == "json"
    }

    fun isXMLFormat(): Boolean {
        return hasFormatSet() && this["_format"] == "xml"
    }

    fun getParamsString(): String {
        val builder = StringBuilder()
        for (key in params.keys) {
            var value = params[key]
            if (value != null) {
                try {
                    value = URLEncoder.encode(value.toString(), "utf-8")
                    if (builder.isNotEmpty()) builder.append("&")
                    builder.append(key).append("=").append(value)
                } catch (e: UnsupportedEncodingException) {
                }
            }
        }
        return builder.toString()
    }

    fun getCsvHeaderParamsString(): String {
        val builder = StringBuilder()
        for (key in params.keys) {
            try {
                val var1: String = URLEncoder.encode(key, "utf-8")
                if (builder.isNotEmpty()) builder.append(",")
                builder.append(var1)
            } catch (e: UnsupportedEncodingException) {
            }
        }
        return builder.toString()
    }

    fun getCsvValueParamsString(): String {
        val builder = StringBuilder()
        val list: List<Any> = ArrayList<Any>(params.keys)
        for (i in list.indices) {
            var value = params[list[i]]
            if (value == null) value = ""
            try {
                value = value.toString()
                if (builder.isNotEmpty() || i == 1) builder.append(",")
                if (value.contains(",")) value = String.format("\"%s\"", value)
                builder.append(value)
            } catch (e: Exception) {
            }
        }
        return builder.toString()
    }

    fun getParamsJson(): JSONObject {
        val obj = JSONObject()
        for (key in params.keys) {
            val value = params[key]
            value?.let {
                try {
                    obj.put(key, it)
                } catch (e: JSONException) {
                }
            }
        }
        return obj
    }
}