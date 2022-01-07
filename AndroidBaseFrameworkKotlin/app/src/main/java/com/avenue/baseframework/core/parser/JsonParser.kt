package com.avenue.baseframework.core.parser

import com.avenue.baseframework.core.helpers.EString
import com.google.gson.Gson
import com.google.gson.internal.`$Gson$Types`
import java.io.InputStream
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.*

class JsonParser<T> {

    private val json: String = EString.EMPTY
    private var type: Type? = null
    private var rawType: Class<in T>? = null

    init {
        type = getSuperclassTypeParameter(javaClass)
        rawType = `$Gson$Types`.getRawType(type) as Class<in T>?
    }

    companion object {
        fun getSuperclassTypeParameter(subclass: Class<*>): Type? {
            val superclass = subclass.genericSuperclass
            if (superclass is Class<*>) {
                throw RuntimeException("Missing type parameter.")
            }
            val parameterized = superclass as ParameterizedType
            return `$Gson$Types`.canonicalize(parameterized.actualTypeArguments[0])
        }

        fun parseToJson(obj: Any?): String? {
            return Gson().toJson(obj)
        }

        fun parseToJson(inputStream: InputStream?): String? {
            val s = Scanner(inputStream).useDelimiter("\\A")
            return if (s.hasNext()) s.next() else ""
        }
    }

    fun parseToJsonWithType(obj: Any?): String? {
        return Gson().toJson(obj, rawType)
    }
}