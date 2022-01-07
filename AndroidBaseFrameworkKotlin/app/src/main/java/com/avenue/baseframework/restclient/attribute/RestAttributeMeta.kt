package com.avenue.baseframework.restclient.attribute

import java.io.Serializable

class RestAttributeMeta() : Serializable {

    var name: String = ""
    var datatype = 0
    var isRequired = false
    var isUniqueId = false

    constructor(name: String) : this() {
        this.name = name
    }

    fun setName(name: String?): RestAttributeMeta {
        this.name = name ?: ""
        return this
    }

    fun setDataType(value: Int): RestAttributeMeta {
        datatype = value
        return this
    }

    fun setRequired(value: Boolean): RestAttributeMeta {
        isRequired = value
        return this
    }

    fun setUniqueId(value: Boolean): RestAttributeMeta {
        isUniqueId = value
        return this
    }

    fun getDataTypeAsString(): String? {
        return when (this.datatype) {
            0 -> "TEXT"
            1 -> "INTEGER"
            2 -> "LONG"
            3 -> "FLOAT"
            4 -> "DOUBLE"
            5 -> "BOOLEAN"
            6 -> "DATETIME"
            7 -> "BLOB"
            else -> null
        }
    }

    fun parseDataTypeFromString(datatype: String?): Int {
        if (datatype != null) {
            if (datatype.equals("TEXT", ignoreCase = true)) {
                return 0
            }
            if (datatype.equals("INTEGER", ignoreCase = true)) {
                return 1
            }
            if (datatype.equals("LONG", ignoreCase = true)) {
                return 2
            }
            if (datatype.equals("FLOAT", ignoreCase = true)) {
                return 3
            }
            if (datatype.equals("DOUBLE", ignoreCase = true)) {
                return 4
            }
            if (datatype.equals("BOOLEAN", ignoreCase = true)) {
                return 5
            }
            if (datatype.equals("DATETIME", ignoreCase = true)) {
                return 6
            }
            if (datatype.equals("BLOB", ignoreCase = true)) {
                return 7
            }
        }
        return -1
    }

    fun isText(): Boolean {
        return this.datatype == 0
    }

    fun isNumeric(): Boolean {
        return isInteger() || isDecimal()
    }

    fun isDecimal(): Boolean {
        return this.datatype == 3 || this.datatype == 4
    }

    fun isInteger(): Boolean {
        return this.datatype == 1 || this.datatype == 2
    }

    fun isDateTime(): Boolean {
        return this.datatype == 6
    }

    fun isBoolean(): Boolean {
        return this.datatype == 5
    }

    fun isBLOB(): Boolean {
        return this.datatype == 7
    }

    override fun equals(obj: Any?): Boolean {
        if (obj is RestAttributeMeta) {
            return obj.name.equals(this.name, ignoreCase = true)
        }
        return false
    }

    override fun toString(): String {
        return this.name + " - " + getDataTypeAsString() + (if (this.isUniqueId) ", resourceid" else "") + if (this.isRequired) ", required" else ""
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + datatype
        result = 31 * result + isRequired.hashCode()
        result = 31 * result + isUniqueId.hashCode()
        return result
    }

    interface DataType {
        companion object {
            const val TEXT = 0
            const val INTEGER = 1
            const val LONG = 2
            const val FLOAT = 3
            const val DOUBLE = 4
            const val BOOLEAN = 5
            const val DATETIME = 6
            const val BLOB = 7
        }
    }
}