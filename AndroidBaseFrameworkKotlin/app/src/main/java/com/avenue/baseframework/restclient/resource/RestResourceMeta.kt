package com.avenue.baseframework.restclient.resource

import com.avenue.baseframework.restclient.attribute.RestAttributeMeta
import java.io.Serializable
import java.util.*

class RestResourceMeta(): Serializable {

    var name: String? = null
    var tableName: String? = null
    private var uniqueColumn: String? = null
    private var metaAttributes: MutableList<RestAttributeMeta>? = null

    constructor(name: String, tableName: String) : this() {
        this.name = name
        this.tableName = tableName
    }

    protected fun setName(name: String?): RestResourceMeta {
        this.name = name!!
        return this
    }

    protected fun setTableName(name: String?): RestResourceMeta {
        tableName = name!!
        return this
    }

    fun setUniqueColumn(uniqueColumn: String?): RestResourceMeta {
        this.uniqueColumn = uniqueColumn
        return this
    }

    fun getUniqueColumn(): String? {
        if (uniqueColumn == null) {
            this.lookupUniqueColumn()
        }
        return uniqueColumn
    }

    private fun lookupUniqueColumn() {
        for (i in getListAttributeMeta().indices) {
            val metaAttr = getListAttributeMeta()[i]
            if (metaAttr.isUniqueId) {
                setUniqueColumn(metaAttr.name)
                break
            }
        }
    }

    protected fun setListAttributeMeta(metaAttributes: MutableList<RestAttributeMeta>): RestResourceMeta {
        this.metaAttributes = metaAttributes
        lookupUniqueColumn()
        return this
    }

    fun getListAttributeMeta(): MutableList<RestAttributeMeta> {
        if (metaAttributes == null) {
            metaAttributes = ArrayList()
        }
        return metaAttributes!!
    }

    fun hasAttributeMeta(attrMeta: RestAttributeMeta): Boolean {
        return getListAttributeMeta().contains(attrMeta)
    }

    fun getAttributeMeta(name: String?): RestAttributeMeta? {
        metaAttributes?.let {
            for (i in it.indices) {
                if (it[i].name.equals(name, true)) {
                    return it[i]
                }
            }
        }
        return null
    }

    override fun equals(obj: Any?): Boolean {
        obj?.let {
            if (it is RestResourceMeta) {
                if (it.name != null && this.name != null && it.tableName != null && this.tableName != null) {
                    return it.name
                        .equals(this.name, ignoreCase = true) && it.tableName
                        .equals(this.tableName, ignoreCase = true)
                }
                return false
            }
            return false
        }
        return false
    }

    override fun toString(): String {
        return this.name + " Set (Table: " + this.tableName + ", UColumn: " + getUniqueColumn() + ")"
    }

    override fun hashCode(): Int {
        var result = name?.hashCode() ?: 0
        result = 31 * result + (tableName?.hashCode() ?: 0)
        result = 31 * result + (uniqueColumn?.hashCode() ?: 0)
        result = 31 * result + (metaAttributes?.hashCode() ?: 0)
        return result
    }
}