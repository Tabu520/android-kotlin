package com.avenue.baseframework.restclient.utils

import java.io.Serializable
import kotlin.collections.HashMap

class RestQuerySelect: Serializable {

    private var map: MutableMap<Any, Any> = hashMapOf()

    fun select(vararg selectClause: String): String {
        return run {
            val strb = StringBuffer()
            val var3: Array<out String> = selectClause
            val var4 = selectClause.size
            for (var5 in 0 until var4) {
                val s = var3[var5]
                when {
                    s.startsWith("$") -> strb.append(s.substring(1)).append(",")
                    s.indexOf(Char(46)) > 0 -> {
                        val tokens = s.split("\\.").toTypedArray()
                        this.handleTokens(tokens, 0, map)
                    }
                    else -> strb.append(s).append(",")
                }
            }
            this.mapToString(strb, map)
            if (strb.toString().endsWith(",")) {
                strb.deleteCharAt(strb.toString().length - 1)
            }
            strb.toString()
        }
    }

    private fun mapToString(strb: StringBuffer, map: MutableMap<Any, Any>) {
        val set = map.entries
        val var4: Iterator<*> = set.iterator()
        while (true) {
            while (var4.hasNext()) {
                val entry = var4.next() as Map.Entry<Any, Any>
                val key = entry.key as String
                val value = entry.value as MutableMap<Any, Any>?
                if (value != null && value.isNotEmpty()) {
                    strb.append(key).append("{")
                    mapToString(strb, value)
                    if (strb.toString().endsWith(",")) {
                        strb.deleteCharAt(strb.toString().length - 1)
                    }
                    strb.append("},")
                } else {
                    strb.append(key).append(",")
                }
            }
            return
        }
    }

    private fun handleTokens(tokens: Array<String>, index: Int, selectMap: MutableMap<Any, Any>) {
        if (tokens.size >= index + 1) {
            val key = tokens[index]
            val map2 = selectMap[key] as HashMap<Any, Any>
            handleTokens(tokens, index + 1, map2)
        }
    }
}