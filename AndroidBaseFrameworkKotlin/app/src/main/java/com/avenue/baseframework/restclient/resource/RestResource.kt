package com.avenue.baseframework.restclient.resource

import com.avenue.baseframework.restclient.attribute.RestAttribute
import com.avenue.baseframework.restclient.resourceset.RestResourceSet
import com.avenue.baseframework.restclient.utils.*
import java.util.*

interface RestResource : RestRequester, RestResponseParser, RestEntity {

    var mIndex: Int
    var mAttributeList: MutableList<RestAttribute>?
    var mIsHidden: Boolean
    var mIsReadOnly: Boolean
    var mIsLoaded: Boolean
    var mIsModified: Boolean
    var mToBeAdded: Boolean
    var mToBeDeleted: Boolean
    var mThisSet: RestResourceSet?
    var mUniqueID: String?
    var mRowStamp: String?
    var mRelatedSets: MutableMap<String, RestResourceSet>?

    fun getMeta(): RestResourceMeta?

    fun getMaximoRestConnector(): MaximoRestConnector?

    fun getName(): String?

    fun getTableName(): String?

    fun setUniqueID(uniqueId: String?): RestResource?

    fun getUniqueColumn(): String?

    fun getSelectClause(): String?

    fun setIndex(index: Int): RestResource?

    @Throws(RestException::class)
    fun reloadFromServer(vararg attributes: String)

    fun setAttributes(newList: MutableList<RestAttribute>?, overWrite: Boolean): RestResource?

    fun getAttributes(): MutableList<RestAttribute>?

    fun hasAttributes(): Boolean

    fun containsAttribute(name: String): Boolean

    fun containsAttribute(attr: RestAttribute?): Boolean

    @Throws(RestException::class)
    fun addAttribute(attributeName: String): RestAttribute?

    @Throws(RestException::class)
    fun addAttribute(attributeName: String, value: Any?): RestResource?

    @Throws(RestException::class)
    fun addAttribute(attr: RestAttribute?): RestResource?

    fun getAttribute(index: Int): RestAttribute?

    fun getAttribute(name: String?): RestAttribute?

    fun setLoaded(isLoaded: Boolean): RestResource?

    fun setModified(isModified: Boolean): RestResource?

    fun setToBeAdded(toBeAdded: Boolean): RestResource?

    fun toBeAdded(): Boolean

    fun setToBeDeleted(toBeDeleted: Boolean): RestResource?

    fun toBeDeleted(): Boolean

    fun toBeSaved(): Boolean

    fun isNull(attribute: String?): Boolean

    fun getValue(attribute: String?): Any?

    fun getString(attribute: String?): String?

    fun getBoolean(attribute: String?): Boolean?

    fun getInt(attribute: String?, locale: Locale?): Int?

    fun getInt(attribute: String?): Int?

    fun getLong(attribute: String?, locale: Locale?): Long?

    fun getLong(attribute: String?): Long?

    fun getFloat(attribute: String?, locale: Locale?): Float?

    fun getFloat(attribute: String?): Float?

    fun getDouble(attribute: String?, locale: Locale?): Double?

    fun getDouble(attribute: String?): Double?

    fun getDate(attribute: String?): Date?

    fun getBytes(attribute: String?): ByteArray?

    @Throws(RestException::class)
    fun setValue(attributeName: String?, value: Any?): RestResource?

    @Throws(RestException::class)
    fun setNull(attributeName: String?): RestResource?

    fun discardChanges(): RestResource?

    fun setHidden(isHidden: Boolean): RestResource

    fun setReadOnly(isReadOnly: Boolean): RestResource?

    fun getNewAttributeInstance(name: String?): RestAttribute?

    fun getRelatedSet(name: String?): RestResourceSet?

    fun getRelatedSetList(): MutableMap<String, RestResourceSet>?
}