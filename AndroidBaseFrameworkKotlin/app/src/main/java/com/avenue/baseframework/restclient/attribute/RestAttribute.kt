package com.avenue.baseframework.restclient.attribute

import com.avenue.baseframework.restclient.resource.RestResource
import com.avenue.baseframework.restclient.utils.RestEntity
import com.avenue.baseframework.restclient.utils.RestException
import com.avenue.baseframework.restclient.utils.RestResponseParser
import java.util.*

interface RestAttribute: RestResponseParser, RestEntity {

    var mAttributeMeta: RestAttributeMeta?
    var mRestResource: RestResource?
    var mInitialContent: Any?
    var mPreviousContent: Any?
    var mCurrentContent: Any?
    var mLocaleContent: String?
    var mIsHidden: Boolean
    var mIsReadOnly: Boolean
    var mIsChanged: Boolean

    fun getMeta(): RestAttributeMeta?

    fun getName(): String?

    fun getOwner(): RestResource?

    fun resolveDataType()

    fun setDataType(dataType: Int): RestAttribute?

    fun getDataType(): Int

    fun getDataTypeAsString(): String?

    fun getValue(): Any?

    fun getString(): String?

    fun getBoolean(): Boolean?

    fun getInt(locale: Locale?): Int?

    fun getInt(): Int?

    fun getLong(locale: Locale?): Long?

    fun getLong(): Long?

    fun getFloat(locale: Locale?): Float?

    fun getFloat(): Float?

    fun getDouble(locale: Locale?): Double?

    fun getDouble(): Double?

    fun getDate(): Date?

    fun getBytes(): ByteArray?

    fun getInitialValue(): Any?

    fun getPreviousValue(): Any?

    @Throws(RestException::class)
    fun setValue(value: Any?): RestAttribute?

    fun rollbackToInitialValue(): RestAttribute?

    @Throws(RestException::class)
    fun undo(): RestAttribute?

    fun getLocaleValue(): String?

    fun setRequired(isRequired: Boolean): RestAttribute?

    fun setUniqueId(isUniqueId: Boolean): RestAttribute?

    fun setChanged(isChanged: Boolean): RestAttribute?

    fun isNull(): Boolean

    fun isText(): Boolean

    fun isNumeric(): Boolean

    fun isDecimal(): Boolean

    fun isInteger(): Boolean

    fun isDateTime(): Boolean

    fun isBoolean(): Boolean

    fun isBLOB(): Boolean

    fun isUniqueId(): Boolean

    fun setHidden(isHidden: Boolean): RestAttribute?

    fun setReadOnly(isReadOnly: Boolean): RestAttribute?
}