package com.avenue.baseframework.restclient.attribute

import android.os.Build
import com.avenue.baseframework.restclient.resource.RestResource
import com.avenue.baseframework.restclient.utils.RestConstants.REST_DATE_RESPONSE_FORMAT
import com.avenue.baseframework.restclient.utils.RestException
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.text.NumberFormat
import java.text.ParseException
import java.util.*

class RestAttributeImpl : RestAttribute, RestAttributeMeta.DataType {

    override var mAttributeMeta: RestAttributeMeta? = null
    override var mRestResource: RestResource? = null
    override var mInitialContent: Any? = null
    override var mPreviousContent: Any? = null
    override var mCurrentContent: Any? = null
    override var mLocaleContent: String? = null
    override var mIsHidden: Boolean = false
    override var mIsReadOnly: Boolean = false
    override var mIsChanged: Boolean = false

    constructor()

    constructor(name: String?, restResource: RestResource) {
        mIsHidden = false
        mIsReadOnly = false
        mIsChanged = false
        this.setName(name)
        this.setOwner(restResource)
    }

    constructor(name: String) : this(name, Any() as RestResource)

    constructor(restResource: RestResource) : this(null, restResource)

    constructor(name: String?, value: Any?, restResource: RestResource) : this(name, restResource) {
        value?.let {
            mCurrentContent = it
            mPreviousContent = it
            mInitialContent = it
        }
        resolveDataType()
    }

    protected fun initMeta(): RestAttributeMeta {
        return RestAttributeMeta()
    }

    protected fun setMeta(meta: RestAttributeMeta): RestAttribute {
        mAttributeMeta = meta
        return this
    }

    override fun getMeta(): RestAttributeMeta? {
        mAttributeMeta?.let {
            setMeta(initMeta())
        }

        return this.mAttributeMeta
    }

    override fun getName(): String? {
        getMeta()?.let {
            return it.name
        }
        return null
    }

    override fun getOwner(): RestResource? {
        return mRestResource
    }

    override fun resolveDataType() {
        if (this.getValue() is ByteArray) {
            setDataType(7)
        } else if (this.getValue() !is Date && getDate() == null) {
            if (this.getValue() !is Boolean && getBoolean() == null) {
                if (this.getValue() !is Int && this.getInt() == null) {
                    if (this.getValue() is Float || this.getFloat() != null) {
                        setDataType(if (this.getValue() !is Double && this.getDouble() == null) 3 else 4)
                    }
                } else {
                    setDataType(if (this.getValue() !is Long && this.getLong() == null) 1 else 2)
                }
            } else {
                setDataType(5)
            }
        } else {
            setDataType(6)
        }
    }

    override fun setDataType(dataType: Int): RestAttribute {
        getMeta()!!.setDataType(dataType)
        return this
    }

    override fun getDataType(): Int {
        return getMeta()!!.datatype
    }

    override fun getDataTypeAsString(): String? {
        return getMeta()!!.getDataTypeAsString()
    }

    override fun getValue(): Any? {
        return mCurrentContent
    }

    override fun getString(): String? {
        return this.getStringFromValue(true)
    }

    private fun getStringFromValue(removeApostrophe: Boolean): String? {
        var ret: String? = null
        if (mCurrentContent != null) {
            if (getDataType() == 7 && this.getValue() is ByteArray) {
                ret = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Base64.getEncoder().encodeToString(mCurrentContent as ByteArray?)
                } else {
                    android.util.Base64.encodeToString(
                        mCurrentContent as ByteArray?,
                        android.util.Base64.DEFAULT
                    );
                }
            } else {
                ret = mCurrentContent.toString()
                val apos = "\""
                if (removeApostrophe && ret.indexOf(apos) == 0 && ret.substring(ret.length - 1) == apos) {
                    ret = ret.substring(1, ret.length - 1)
                }
            }
        }
        return ret
    }

    override fun getBoolean(): Boolean? {
        return if (this.getValue() is Boolean) {
            this.getValue() as Boolean?
        } else {
            val value = getString()
            if (value != null) {
                if (value.equals("true", ignoreCase = true) || value == "1") {
                    return true
                }
                if (value.equals("false", ignoreCase = true) || value == "0") {
                    return false
                }
            }
            null
        }
    }

    private fun getNumberFromString(locale: Locale): Number? {
        val nf = NumberFormat.getNumberInstance(locale)
        return try {
            val value = getStringFromValue(false)
            value?.let {
                String.format(locale, "%.2f", nf.parse(value)!!.toDouble()).toDouble()
            }
            throw NullPointerException()
        } catch (var5: ParseException) {
            null
        } catch (var5: NumberFormatException) {
            null
        } catch (var5: NullPointerException) {
            null
        }
    }

    override fun getInt(locale: Locale?): Int? {
        return if (this.getValue() is Int) {
            this.getValue() as Int?
        } else {
            locale?.let {
                val formattedNum: Number = getNumberFromString(it)!!
                formattedNum.toInt()
            }
            0 // if var1 is null, return 0
        }
    }

    override fun getInt(): Int? {
        return this.getInt(Locale.getDefault())
    }

    override fun getLong(locale: Locale?): Long? {
        return if (this.getValue() is Long) {
            this.getValue() as Long?
        } else {
            val formattedNum = getNumberFromString(locale!!)
            formattedNum?.toLong()
        }
    }

    override fun getLong(): Long? {
        return this.getLong(Locale.getDefault())
    }

    override fun getFloat(locale: Locale?): Float? {
        return if (this.getValue() is Float) {
            this.getValue() as Float?
        } else {
            val formattedNum = getNumberFromString(locale!!)
            formattedNum?.toFloat()
        }
    }

    override fun getFloat(): Float? {
        return this.getFloat(Locale.getDefault())
    }

    override fun getDouble(locale: Locale?): Double? {
        return if (this.getValue() is Double) {
            this.getValue() as Double?
        } else {
            val formattedNum = getNumberFromString(locale!!)
            formattedNum?.toDouble()
        }
    }

    override fun getDouble(): Double? {
        return this.getDouble(Locale.getDefault())
    }

    override fun getDate(): Date? {
        return if (this.getValue() is Date) {
            this.getValue() as Date?
        } else {
            try {
                REST_DATE_RESPONSE_FORMAT.parse(getStringFromValue(true)!!)
            } catch (var2: ClassCastException) {
                null
            } catch (var2: ParseException) {
                null
            } catch (var2: NullPointerException) {
                null
            }
        }
    }

    override fun getBytes(): ByteArray? {
        return if (this.getValue() is ByteArray) {
            this.getValue() as ByteArray?
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Base64.getDecoder().decode(getString())
            } else {
                android.util.Base64.decode(getString(), android.util.Base64.DEFAULT)
            }
        }
    }

    override fun getInitialValue(): Any? {
        return mInitialContent
    }

    override fun getPreviousValue(): Any? {
        return mPreviousContent
    }

    protected fun setName(name: String?): RestAttribute {
        getMeta()!!.setName(name?.uppercase(Locale.getDefault()))
        return this
    }

    protected fun setOwner(res: RestResource?): RestAttribute {
        mRestResource = res
        return this
    }

    override fun setValue(value: Any?): RestAttribute {
        return if (!mIsReadOnly) {
            mPreviousContent = this.getValue()
            mCurrentContent = value
            setChanged(true)
            this
        } else {
            throw RestException("Attribute " + getName() + " is readonly!")
        }
    }

    override fun rollbackToInitialValue(): RestAttribute {
        if (mIsChanged) {
            mCurrentContent = getInitialValue()
            mPreviousContent = getInitialValue()
            setChanged(false)
        }
        return this
    }

    override fun undo(): RestAttribute {
        if (mIsChanged) {
            this.setValue(getPreviousValue())
        }
        return this
    }

    override fun getLocaleValue(): String? {
        return mLocaleContent
    }

    override fun setRequired(isRequired: Boolean): RestAttribute {
        getMeta()!!.setRequired(isRequired)
        return this
    }

    override fun setUniqueId(isUniqueId: Boolean): RestAttribute {
        getMeta()!!.setUniqueId(isUniqueId)
        return this
    }

    override fun setChanged(isChanged: Boolean): RestAttribute {
        mIsChanged = isChanged
        return this
    }

    override fun isNull(): Boolean {
        return getValue() == null
    }

    override fun isText(): Boolean {
        return getMeta()!!.isText()
    }

    override fun isNumeric(): Boolean {
        return getMeta()!!.isNumeric()
    }

    override fun isDecimal(): Boolean {
        return getMeta()!!.isDecimal()
    }

    override fun isInteger(): Boolean {
        return getMeta()!!.isInteger()
    }

    override fun isDateTime(): Boolean {
        return getMeta()!!.isDateTime()
    }

    override fun isBoolean(): Boolean {
        return getMeta()!!.isBoolean()
    }

    override fun isBLOB(): Boolean {
        return getMeta()!!.isBLOB()
    }

    override fun isUniqueId(): Boolean {
        return getMeta()!!.isUniqueId
    }

    override fun setHidden(isHidden: Boolean): RestAttribute {
        mIsHidden = isHidden
        return this
    }

    override fun setReadOnly(isReadOnly: Boolean): RestAttribute {
        mIsReadOnly = isReadOnly
        return this
    }

    override fun parseJsonResponse(parser: Any?) {
        try {
            val jsonParser: JsonParser = parser as JsonParser
            var token: JsonToken = jsonParser.nextToken()
            while (token !== JsonToken.END_OBJECT) {
                if (token === JsonToken.FIELD_NAME) {
                    val fieldName: String = jsonParser.currentName
                    token = jsonParser.nextToken()
                    if (fieldName == "hidden") {
                        setHidden(jsonParser.booleanValue)
                    } else if (fieldName == "readonly") {
                        setReadOnly(jsonParser.booleanValue)
                    } else if (fieldName == "required") {
                        setRequired(jsonParser.booleanValue)
                    } else if (fieldName == "resourceid") {
                        setUniqueId(jsonParser.booleanValue)
                    } else if (fieldName != "content") {
                        if (fieldName == "localecontent") {
                            this.mLocaleContent = jsonParser.text
                        } else {
                            getMeta()!!.setName(fieldName)
                        }
                    } else {
                        var contentValue: Any? = null
                        if (token === JsonToken.VALUE_STRING) {
                            contentValue = jsonParser.text
                            setDataType(0)
                        } else if (token === JsonToken.VALUE_NUMBER_INT) {
                            contentValue = jsonParser.intValue
                            setDataType(1)
                        } else if (token === JsonToken.VALUE_NUMBER_FLOAT) {
                            contentValue = jsonParser.doubleValue
                            setDataType(4)
                        } else if (token !== JsonToken.VALUE_TRUE && token !== JsonToken.VALUE_FALSE) {
                            if (token === JsonToken.VALUE_NULL) {
                                contentValue = null
                            }
                        } else {
                            contentValue = jsonParser.booleanValue
                            setDataType(5)
                        }
                        this.mInitialContent = contentValue
                        this.mCurrentContent = contentValue
                    }
                }
                token = jsonParser.nextToken()
            }
            this.onResponseParsed()
        } catch (var6: IOException) {
            throw RestException(var6)
        }
    }

    protected fun onResponseParsed() {}

    override fun parseXMLResponse(parser: Any?) {
        try {
            val xmlParser = parser as XmlPullParser
            var event = xmlParser.eventType
            while (event != 1) {
                var parsedContent: String
                if (event == 2) {
                    parsedContent = xmlParser.getAttributeValue(null as String?, "hidden")
                    if (parsedContent != null) {
                        setHidden(parsedContent.toInt() == 1)
                    }
                    val isReadOnly = xmlParser.getAttributeValue(null as String?, "readonly")
                    if (isReadOnly != null) {
                        setReadOnly(isReadOnly.toInt() == 1)
                    }
                    val isRequired = xmlParser.getAttributeValue(null as String?, "required")
                    if (isRequired != null) {
                        setRequired(isRequired.toInt() == 1)
                    }
                    val isResourceId = xmlParser.getAttributeValue(null as String?, "resourceid")
                    if (isResourceId != null) {
                        setUniqueId(isResourceId.toInt() == 1)
                    }
                }
                if (event == 4) {
                    parsedContent = xmlParser.text
                    this.mInitialContent = parsedContent
                    this.mCurrentContent = parsedContent
                    this.mLocaleContent = parsedContent
                }
                if (event == 3) {
                    break
                }
                event = xmlParser.next()
            }
            resolveDataType()
            onResponseParsed()
        } catch (var8: XmlPullParserException) {
            throw RestException(var8)
        } catch (var8: IOException) {
            throw RestException(var8)
        }
    }

    override fun canSaveToServer(): Boolean {
        return !isUniqueId() && !isNull()
    }

    override fun validateForServer() {

    }

    override fun close() {
        synchronized(this) {
            setMeta((null as RestAttributeMeta?)!!)
            this.mRestResource = null
            this.mInitialContent = null
            this.mPreviousContent = null
            this.mCurrentContent = null
            this.mLocaleContent = null
        }

        this.onClosed()
    }

    protected fun onClosed() {}

    override fun toString(): String {
        return getMeta().toString() + (if (mIsHidden) ", hidden" else "") + (if (mIsReadOnly) ", readonly" else "") + (if (mIsChanged) ", changed" else "") + " = " + getString()
    }

    override fun equals(other: Any?): Boolean {
        return other is RestAttribute && other.getMeta()!! == getMeta()
    }

    override fun hashCode(): Int {
        var result = mAttributeMeta?.hashCode() ?: 0
        result = 31 * result + (mRestResource?.hashCode() ?: 0)
        result = 31 * result + (mInitialContent?.hashCode() ?: 0)
        result = 31 * result + (mPreviousContent?.hashCode() ?: 0)
        result = 31 * result + (mCurrentContent?.hashCode() ?: 0)
        result = 31 * result + (mLocaleContent?.hashCode() ?: 0)
        result = 31 * result + mIsHidden.hashCode()
        result = 31 * result + mIsReadOnly.hashCode()
        result = 31 * result + mIsChanged.hashCode()
        return result
    }
}