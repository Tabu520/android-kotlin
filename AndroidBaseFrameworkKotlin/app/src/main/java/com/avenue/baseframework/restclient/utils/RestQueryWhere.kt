package com.avenue.baseframework.restclient.utils

import java.io.Serializable
import java.io.UnsupportedEncodingException
import java.lang.Exception
import java.net.URLEncoder
import java.util.*
import javax.xml.datatype.DatatypeConfigurationException

class RestQueryWhere: Serializable {

    private val map: MutableMap<String, Any> = linkedMapOf()
    private var currentKey: String? = null

    fun where(name: String): RestQueryWhere {
        currentKey = name.uppercase(Locale.getDefault())
        return this
    }

    fun and(name: String): RestQueryWhere {
        if (name.indexOf(Char(46)) > 0) {
            val attrPath = name.split("\\.").toTypedArray()
            var childMap: MutableMap<String, Any>? = map[attrPath[0]] as MutableMap<String, Any>
            if (childMap == null) {
                childMap = linkedMapOf()
                map[attrPath[0]] = childMap
            }
        }
        return where(name)
    }

    private fun getCurrentMap(): MutableMap<String, Any> {
        return if (currentKey!!.indexOf(Char(46)) > 0) {
            val attrPath = currentKey!!.split("\\.").toTypedArray()
            map[attrPath[0]] as MutableMap<String, Any>
        } else {
            map
        }
    }

    private fun getCurrentKey(): String {
        return if (currentKey!!.indexOf(Char(46)) > 0) {
            val attrPath = currentKey!!.split("\\.").toTypedArray()
            attrPath[1]
        } else {
            currentKey!!
        }
    }

    private fun setQueryToken(s: String) {
        val currMap = getCurrentMap()
        var currKey = getCurrentKey()
        if (currMap.containsKey(currKey)) {
            currKey = "/$currKey"
        }
        currMap[currKey] = s
    }

    @Throws(RestException::class)
    private fun setQueryTokenValue(token: String, value: Any) {
        try {
            setQueryToken(RestUtils.stringValue(token + value))
        } catch (var4: DatatypeConfigurationException) {
            throw RestException(var4.message!!, var4)
        } catch (var4: UnsupportedEncodingException) {
            throw RestException(var4.message!!, var4)
        }
    }

    @Throws(RestException::class)
    fun equalTo(value: Any?): RestQueryWhere {
        setQueryTokenValue("~eq~", value!!)
        return this
    }

    @Throws(RestException::class)
    fun notEqualTo(value: Any?): RestQueryWhere {
        setQueryTokenValue("~neq~", value!!)
        return this
    }

    @Throws(RestException::class)
    fun startsWith(value: String?): RestQueryWhere {
        setQueryTokenValue("~sw~", value!!)
        return this
    }

    @Throws(RestException::class)
    fun endsWith(value: String?): RestQueryWhere {
        setQueryTokenValue("~ew~", value!!)
        return this
    }

    @Throws(RestException::class)
    fun like(value: String?): RestQueryWhere {
        setQueryTokenValue("", value!!)
        return this
    }

    @Throws(RestException::class)
    fun greaterThan(value: Any?): RestQueryWhere {
        setQueryTokenValue("~gt~", value!!)
        return this
    }

    @Throws(RestException::class)
    fun greaterThanEqual(value: Any?): RestQueryWhere {
        setQueryTokenValue("~gteq~", value!!)
        return this
    }

    @Throws(RestException::class)
    fun lessThan(value: Any?): RestQueryWhere {
        setQueryTokenValue("~lt~", value!!)
        return this
    }

    @Throws(RestException::class)
    fun lessThanEqual(value: Any?): RestQueryWhere {
        setQueryTokenValue("~lteq~", value!!)
        return this
    }

    @Throws(DatatypeConfigurationException::class, UnsupportedEncodingException::class)
    fun `in`(vararg values: Any?): RestQueryWhere {
        val strb = StringBuffer()
        //strb.append("[");
        for (o in values) {
            strb.append(RestUtils.stringValue(o!!)).append(",")
        }
        var s = strb.toString()
        s = s.substring(0, s.length - 1)
        //strb.append("]");
        setQueryToken(" in [$s]")
        return this
    }

    @Throws(RestException::class)
    fun whereClause(): String {
        return try {
            val strbWhere = StringBuffer()
            val set: Set<Map.Entry<String, Any>> = map.entries
            var cnt = 0
            val var4: Iterator<*> = set.iterator()
            while (var4.hasNext()) {
                val entry: Map.Entry<String, Any> = var4.next() as Map.Entry<String, Any>
                ++cnt
                var key = entry.key
                if (key.startsWith("/")) {
                    key = key.substring(1)
                }
                val value = entry.value
                strbWhere.append(key)
                strbWhere.append("=")
                if (value is String) {
                    strbWhere.append(URLEncoder.encode(value, "utf-8"))
                } else {
                    val childMap: Map<String, Any> = value as Map<String, Any>
                    strbWhere.append("{")
                    val cSet = childMap.entries
                    var ccnt = 0
                    val var11: Iterator<*> = cSet.iterator()
                    while (var11.hasNext()) {
                        val cEntry: Map.Entry<String, String> = var11.next() as Map.Entry<String, String>
                        ++ccnt
                        var cKey = cEntry.key
                        if (cKey.startsWith("/")) {
                            cKey = cKey.substring(1)
                        }
                        strbWhere.append(cKey)
                        strbWhere.append("=")
                        strbWhere.append(URLEncoder.encode(cEntry.value, "utf-8"))
                        if (cSet.size > ccnt) {
                            strbWhere.append("&")
                        }
                    }
                    strbWhere.append("}")
                }
                if (set.size > cnt) {
                    strbWhere.append("&")
                }
            }
            strbWhere.toString()
        } catch (var15: Exception) {
            throw RestException("Errors while constructing where clause from RestQueryWhere object")
        }
    }

}