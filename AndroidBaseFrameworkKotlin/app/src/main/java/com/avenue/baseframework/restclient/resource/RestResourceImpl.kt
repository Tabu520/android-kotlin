package com.avenue.baseframework.restclient.resource

import com.avenue.baseframework.restclient.attribute.RestAttribute
import com.avenue.baseframework.restclient.attribute.RestAttributeImpl
import com.avenue.baseframework.restclient.resourceset.RestResourceSet
import com.avenue.baseframework.restclient.utils.RestConstants.REST_DATE_REQUEST_FORMAT
import com.avenue.baseframework.restclient.mbo.RestMboSet
import com.avenue.baseframework.restclient.utils.*
import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.Reader
import java.lang.Exception
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.*
import kotlin.collections.ArrayList

abstract class RestResourceImpl : RestResource {

    init {
        mIndex = -1
        mIsHidden = false
        mIsReadOnly = false
        mIsLoaded = false
        mIsModified = false
        mToBeAdded = false
        mToBeDeleted = false
        mRowStamp = null
    }

    constructor(index: Int, restResourceSet: RestResourceSet) {
        setThisSet(restResourceSet)
        setIndex(index)
    }

    constructor(restResourceSet: RestResourceSet) : this(restResourceSet.count(), restResourceSet)

    protected fun setThisSet(restResourceSet: RestResourceSet): RestResource {
        this.mThisSet = restResourceSet
        return this
    }

    override fun getMaximoRestConnector(): MaximoRestConnector? {
        mThisSet?.let {
            return it.getMaximoRestConnector()
        }
        return null
    }

    protected open fun setRowStamp(value: String): RestResource? {
        mRowStamp = value
        return this
    }

    override fun getURI(): String? {
        return getMaximoRestConnector()!!.getConnectorURI(this)
            .toString() + "/" + getName() + "/" + mUniqueID
    }

    override fun getMeta(): RestResourceMeta? {
        mThisSet?.let {
            return it.meta
        }
        return null
    }

    override fun getTableName(): String? {
        return getMeta()!!.tableName
    }

    override fun getName(): String? {
        return getMeta()!!.name
    }

    override fun getUniqueColumn(): String? {

        if (mThisSet!!.getUniqueColumn() == null) {
            val var1: Iterator<*> = getAttributes()!!.iterator()
            while (var1.hasNext()) {
                val attr = var1.next() as RestAttribute
                if (attr.isUniqueId()) {
                    mThisSet!!.setUniqueColumn(attr.getName())
                    break
                }
            }
        }
        return mThisSet!!.getUniqueColumn()
    }

    override fun setUniqueID(uniqueId: String?): RestResource? {
        mUniqueID = uniqueId
        return this
    }

    override fun getSelectClause(): String? {
        return mThisSet!!.selectClause
    }

    override fun setIndex(index: Int): RestResource? {
        mIndex = index
        return this
    }

    open fun getIndex(): Int {
        return this.mIndex
    }

    override fun getNewAttributeInstance(name: String?): RestAttribute? {
        return RestAttributeImpl(name, this)
    }

    protected open fun onResponseHandled() {
        setLoaded(true)
    }

    override fun handleResponse(response: InputStream?) {
        try {
            val inputReader: Reader = InputStreamReader(response, StandardCharsets.UTF_8)
            if (getMaximoRestConnector()!!.restParams!!.isJSONFormat()) {
                parseJsonResponse(JsonFactory().createParser(inputReader))
            } else {
                val factory = XmlPullParserFactory.newInstance()
                factory.isNamespaceAware = true
                val xpp = factory.newPullParser()
                xpp.setInput(inputReader)
                parseXMLResponse(xpp)
            }
        } catch (var5: Exception) {
            throw RestException("Errors while parsing the response", var5)
        }
    }

    override fun parseJsonResponse(parser: Any?) {
        try {
            val jsonParser = parser as JsonParser
            var token = jsonParser.nextToken()
            while (token != JsonToken.END_OBJECT) {
                if (token == JsonToken.FIELD_NAME) {
                    var fieldName = jsonParser.currentName
                    token = jsonParser.nextToken()
                    when (fieldName) {
                        "rowstamp" -> setRowStamp(jsonParser.valueAsString)
                        "hidden" -> setHidden(jsonParser.booleanValue)
                        "readonly" -> setReadOnly(jsonParser.booleanValue)
                        "Attributes" -> {
                            var newAttrList: MutableList<RestAttribute> = ArrayList()
                            if (token == JsonToken.START_OBJECT) {
                                while (token != JsonToken.END_OBJECT) {
                                    token = jsonParser.nextToken()
                                    if (token == JsonToken.FIELD_NAME) {
                                        val attrName = jsonParser.currentName
                                        var newAttr = this.getAttribute(attrName)
                                        if (newAttr == null) {
                                            newAttr = getNewAttributeInstance(attrName)
                                            newAttrList.add(newAttr!!)
                                        }
                                        newAttr.parseJsonResponse(parser)
                                        this.onAttributeFetched(newAttr)
                                    }
                                }
                            }
                            setAttributes(newAttrList, false)
                        }
                        "RelatedMbos" -> {
                            token = jsonParser.currentToken
                            while (token != JsonToken.END_OBJECT) {
                                if (token == JsonToken.FIELD_NAME) {
                                    fieldName = jsonParser.currentName
                                } else if (token == JsonToken.START_ARRAY) {
                                    while (token != JsonToken.END_OBJECT && token != JsonToken.FIELD_NAME) {
                                        if (token == JsonToken.START_ARRAY) {
                                            val newSet =
                                                RestMboSet(fieldName, getMaximoRestConnector()!!)
                                            newSet.owner = this
                                            newSet.relationshipName = fieldName
                                            newSet.parseJsonResponse(parser)
                                            getRelatedSetList()!![fieldName] = newSet
                                        } else if (token == JsonToken.END_ARRAY) {
                                            break
                                        }
                                        token = jsonParser.currentToken
                                    }
                                }
                                token = jsonParser.nextToken()
                            }
                        }
                    }
                }
                token = jsonParser.nextToken()
            }
            onResponseHandled()
        } catch (var8: IOException) {
            throw RestException(var8)
        }
    }

    override fun parseXMLResponse(parser: Any?) {
        try {
            val xmlParser = parser as XmlPullParser
            var event = xmlParser.eventType
            label70@ while (event != 1) {
                event = xmlParser.eventType
                var tagName = xmlParser.name
                if (event == 2) {
                    val requiredAttr = xmlParser.getAttributeValue(null as String?, "required")
                    if (requiredAttr != null) {
                        var newAttr = this.getAttribute(tagName)
                        if (newAttr == null) {
                            newAttr = getNewAttributeInstance(tagName)
                            this.addAttribute(newAttr)
                        }
                        newAttr!!.parseXMLResponse(xmlParser)
                        this.onAttributeFetched(newAttr)
                    } else {
                        if (tagName != getTableName()) {
                            while (true) {
                                if (event == 3 && tagName == getTableName()) {
                                    break@label70
                                }
                                if (event == 2) {
                                    var relatedRSSet = getRelatedSet(tagName)
                                    if (relatedRSSet == null) {
                                        relatedRSSet =
                                            RestMboSet(tagName, getMaximoRestConnector()!!)
                                        (relatedRSSet as RestMboSet?)!!.owner = this
                                        (relatedRSSet as RestMboSet?)!!.relationshipName = tagName
                                        relatedRSSet.parseXMLResponse(xmlParser)
                                        getRelatedSetList()!![tagName] = relatedRSSet
                                    }
                                }
                                event = xmlParser.eventType
                                tagName = xmlParser.name
                            }
                        }
                        val hiddenAttr = xmlParser.getAttributeValue(null as String?, "hidden")
                        val readonlyAttr = xmlParser.getAttributeValue(null as String?, "readonly")
                        if (hiddenAttr != null) {
                            setHidden(hiddenAttr.toInt() == 1)
                        }
                        if (readonlyAttr != null) {
                            setReadOnly(readonlyAttr.toInt() == 1)
                        }
                    }
                }
                if (event == 3 && tagName == getTableName()) {
                    break
                }
                xmlParser.next()
            }
            onResponseHandled()
        } catch (var8: XmlPullParserException) {
            throw RestException(var8)
        } catch (var8: IOException) {
            throw RestException(var8)
        }
    }

    protected open fun onAttributeFetched(attr: RestAttribute) {
        if (attr.isUniqueId()) {
            if (getUniqueColumn() == null) {
                this.mThisSet!!.setUniqueColumn(attr.getName())
            }
            mUniqueID = attr.getString()
        }
        val newAttrMeta = attr.getMeta()
        if (!getMeta()!!.hasAttributeMeta(newAttrMeta!!)) {
            getMeta()!!.getListAttributeMeta().add(newAttrMeta)
        }
    }

    @Throws(RestException::class)
    override fun loadFromServer() {
        if (!this.mIsLoaded) {
            try {
                getMaximoRestConnector()!!.restOptions!!.handlerContext = getHandlerContext()
                val uri = getURI()
                if (uri == null || uri.isEmpty()) {
                    throw RestException("The resource is invalid")
                }
                getMaximoRestConnector()!![uri, this]
            } catch (var2: IOException) {
                throw RestException(var2.message!!, var2)
            }
        }
    }

    override fun reloadFromServer(vararg attributes: String) {
        getMaximoRestConnector()!!.restParams!!
            .put("_includecols", RestQuerySelect().select(*attributes))
        this.reloadFromServer()
    }

    @Throws(RestException::class)
    override fun reloadFromServer() {
        if (getURI() != null) {
            setLoaded(false)
            loadFromServer()
        }
    }

    override fun setAttributes(
        newList: MutableList<RestAttribute>?,
        overWrite: Boolean
    ): RestResource? {
        mAttributeList?.let { oldList ->
            if (!overWrite && oldList.isNotEmpty()) {
                newList?.let { new ->
                    for (i in new.indices) {
                        for (j in oldList.indices) {
                            val newRestAttribute = new[i]
                            if (oldList[j].getMeta()!! == newRestAttribute.getMeta()) {
                                oldList[j] = newRestAttribute
                                break
                            }
                        }
                    }
                }
            } else {
                mAttributeList = newList!!
            }
        }

        return this
    }

    override fun getAttributes(): MutableList<RestAttribute>? {
        mAttributeList?.let {
            mAttributeList = mutableListOf()
        }
        return this.mAttributeList
    }

    override fun getAttribute(index: Int): RestAttribute? {
        return if (hasAttributes()) getAttributes()!![index] else null
    }

    override fun getAttribute(name: String?): RestAttribute? {
        if (hasAttributes()) {
            val var2: Iterator<*> = getAttributes()!!.iterator()
            while (var2.hasNext()) {
                val restAttribute = var2.next() as RestAttribute
                if (restAttribute.getName().equals(name, true)) {
                    return restAttribute
                }
            }
        }
        return null
    }

    override fun hasAttributes(): Boolean {
        return getAttributes() != null && getAttributes()!!.isNotEmpty()
    }

    override fun containsAttribute(name: String): Boolean {
        return this.containsAttribute(RestAttributeImpl(name) as RestAttribute)
    }

    override fun containsAttribute(attr: RestAttribute?): Boolean {
        return hasAttributes() && getAttributes()!!.contains(attr!!)
    }

    override fun addAttribute(attributeName: String): RestAttribute? {
        val newAttr = getNewAttributeInstance(attributeName)
        this.addAttribute(newAttr)
        return newAttr
    }

    override fun addAttribute(attributeName: String, value: Any?): RestResource? {
        val newAttr = getNewAttributeInstance(attributeName)
        newAttr!!.setValue(value)
        return this.addAttribute(newAttr)
    }

    @Throws(RestException::class)
    override fun addAttribute(attr: RestAttribute?): RestResource? {
        return if (!this.containsAttribute(attr)) {
            getAttributes()!!.add(attr!!)
            this
        } else {
            throw RestException(
                "Already contains a RestAttribute named '" + attr!!.getName().toString() + "'"
            )
        }
    }

    override fun setLoaded(isLoaded: Boolean): RestResource? {
        this.mIsLoaded = isLoaded
        return this
    }

    open fun isLoaded(): Boolean {
        return this.mIsLoaded
    }

    override fun setModified(isModified: Boolean): RestResource? {
        this.mIsModified = isModified
        return this
    }

    open fun isModified(): Boolean {
        return this.mIsModified
    }

    override fun setToBeAdded(toBeAdded: Boolean): RestResource? {
        this.mToBeAdded = toBeAdded
        return this
    }

    override fun toBeAdded(): Boolean {
        return this.mToBeAdded
    }

    override fun setToBeDeleted(value: Boolean): RestResource? {
        this.mToBeDeleted = value
        if (toBeAdded()) {
            setToBeAdded(false)
        }
        if (isModified()) {
            setModified(false)
        }
        return this
    }

    override fun toBeDeleted(): Boolean {
        return this.mToBeDeleted
    }

    override fun toBeSaved(): Boolean {
        return toBeAdded() && !toBeDeleted() || isModified() || toBeDeleted()
    }

    override fun isNull(attribute: String?): Boolean {
        return !this.containsAttribute(attribute!!) || this.getAttribute(attribute)!!.isNull()
    }

    override fun getValue(attribute: String?): Any? {
        return if (this.containsAttribute(attribute!!))
            this.getAttribute(attribute)!!.getValue()
        else null
    }

    override fun getString(attribute: String?): String? {
        return if (this.containsAttribute(attribute!!))
            this.getAttribute(attribute)!!.getString()
        else null
    }

    override fun getBoolean(attribute: String?): Boolean? {
        return if (this.containsAttribute(attribute!!))
            this.getAttribute(attribute)!!.getBoolean()
        else null
    }

    override fun getInt(attribute: String?, locale: Locale?): Int? {
        return if (this.containsAttribute(attribute!!))
            this.getAttribute(attribute)!!.getInt(locale)
        else null
    }

    override fun getInt(attribute: String?): Int? {
        return if (this.containsAttribute(attribute!!))
            this.getAttribute(attribute)!!.getInt()
        else null
    }

    override fun getLong(attribute: String?, locale: Locale?): Long? {
        return if (this.containsAttribute(attribute!!))
            this.getAttribute(attribute)!!.getLong(locale)
        else null
    }

    override fun getLong(attribute: String?): Long? {
        return if (this.containsAttribute(attribute!!))
            this.getAttribute(attribute)!!.getLong()
        else null
    }

    override fun getFloat(attribute: String?, locale: Locale?): Float? {
        return if (this.containsAttribute(attribute!!))
            this.getAttribute(attribute)!!.getFloat(locale)
        else null
    }

    override fun getFloat(attribute: String?): Float? {
        return if (this.containsAttribute(attribute!!))
            this.getAttribute(attribute)!!.getFloat()
        else null
    }

    override fun getDouble(attribute: String?, locale: Locale?): Double? {
        return if (this.containsAttribute(attribute!!))
            this.getAttribute(attribute)!!.getDouble(locale)
        else null
    }

    override fun getDouble(attribute: String?): Double? {
        return if (this.containsAttribute(attribute!!))
            this.getAttribute(attribute)!!.getDouble()
        else null
    }

    override fun getDate(attribute: String?): Date? {
        return if (this.containsAttribute(attribute!!))
            this.getAttribute(attribute)!!.getDate()
        else null
    }

    override fun getBytes(attribute: String?): ByteArray? {
        return if (this.containsAttribute(attribute!!))
            this.getAttribute(attribute)!!.getBytes()
        else null
    }

    @Throws(RestException::class)
    override fun setValue(attributeName: String?, value: Any?): RestResource? {
        return if (!this.mIsReadOnly) {
            if (this.containsAttribute(attributeName!!)) {
                this.getAttribute(attributeName)!!.setValue(value)
            } else {
                try {
                    this.addAttribute(attributeName)!!.setValue(value)
                } catch (var4: RestException) {
                }
            }
            setModified(true)
            this
        } else {
            throw RestException("Object " + getName() + "(" + getUniqueColumn() + ": " + this.mUniqueID + ") is readonly!")
        }
    }

    @Throws(RestException::class)
    override fun setNull(attributeName: String?): RestResource? {
        this.setValue(attributeName, null as Any?)
        return this
    }

    @Throws(RestException::class)
    override fun validateForServer() {
        val uri = getURI()
        if (uri == null || uri.isEmpty()) {
            throw RestException("The resource is invalid")
        }
    }

    override fun canSaveToServer(): Boolean {
        return true
    }

    @Throws(RestException::class, IOException::class)
    protected open fun putToServer() {
        val params: RestParams = getMaximoRestConnector()!!.restParams!!
        if (params.containsKey("#restquerywhere")) {
            params.remove("#restquerywhere")
        }
        for (attr in getAttributes()!!.toTypedArray()) {
            if (attr.mIsChanged && attr.canSaveToServer()) {
                attr.validateForServer()
                params.put(attr.getName(), if (!attr.isNull()) attr.getString() else null)
            }
        }
        getMaximoRestConnector()!!.put(getURI(), this)
        getMaximoRestConnector()!!.resetToInitialParams()
    }

    @Throws(IOException::class, RestException::class)
    protected open fun postToServer() {
        val body = StringBuilder()
        for (attr in getAttributes()!!.toTypedArray()) {
            if (attr.canSaveToServer()) {
                attr.validateForServer()
                val value: String? = when {
                    attr.isDateTime() -> {
                        REST_DATE_REQUEST_FORMAT.format(attr.getDate()!!)
                    }
                    attr.isDecimal() -> {
                        attr.getDouble()!!.toString()
                    }
                    attr.isBLOB() -> {
                        RestUtils.encodeByteArrayToString(attr.getBytes()!!)
                    }
                    else -> {
                        attr.getString()
                    }
                }
                body.append(attr.getName()).append("=").append(URLEncoder.encode(value, "utf-8"))
                    .append("&")
            }
        }
        getMaximoRestConnector()!!.post(
            this.mThisSet!!.getURI(),
            body.substring(0, body.length - 1),
            this
        )
    }

    @Throws(RestException::class, IOException::class)
    protected open fun deleteOnServer() {
        getMaximoRestConnector()!!.resetToInitialParams()
        getMaximoRestConnector()!!.delete(getURI())
        close()
    }

    @Throws(RestException::class)
    override fun disconnect() {
        try {
            getMaximoRestConnector()!!.disconnect()
            getMaximoRestConnector()!!.closeConnection()
        } catch (var2: IOException) {
            throw RestException(var2.message!!, var2)
        }
    }

    @Throws(RestException::class)
    override fun saveToServer() {
        if (canSaveToServer()) {
            validateForServer()
            try {
                if (!toBeDeleted()) {
                    if (toBeAdded()) {
                        postToServer()
                    } else if (isModified()) {
                        putToServer()
                    }
                    setToBeAdded(false)
                    setModified(false)
                } else {
                    deleteOnServer()
                }
                onSavedToServer()
            } catch (var2: IOException) {
                throw RestException(var2.message!!, var2)
            }
        }
    }

    protected open fun onSavedToServer() {}

    open fun onAdded() {}

    override fun discardChanges(): RestResource? {
        if (this.mIsModified) {
            val var1: Iterator<*> = getAttributes()!!.iterator()
            while (var1.hasNext()) {
                val attr = var1.next() as RestAttribute
                attr.rollbackToInitialValue()
            }
            setModified(false)
        }
        return this
    }

    open fun isHidden(): Boolean {
        return this.mIsHidden
    }

    override fun setHidden(isHidden: Boolean): RestResource {
        this.mIsHidden = isHidden
        return this
    }

    open fun isReadOnly(): Boolean {
        return this.mIsReadOnly
    }

    override fun setReadOnly(isReadOnly: Boolean): RestResource? {
        this.mIsReadOnly = isReadOnly
        return this
    }

    override fun close() {
        synchronized(this) {
            mAttributeList?.let {
                if (it.isNotEmpty()) {
                    val var2: Iterator<*> = getAttributes()!!.iterator()
                    while (var2.hasNext()) {
                        val attr = var2.next() as RestAttribute
                        attr.close()
                    }
                    it.clear()
                }
            }
            mRelatedSets?.let {
                if (it.isNotEmpty()) {
                    it.clear()
                }
            }
            this.mAttributeList = null
            this.mThisSet = null
            this.mUniqueID = null
            this.mRowStamp = null
        }
        onClosed()
    }

    protected open fun onClosed() {}

    override fun getRelatedSet(name: String?): RestResourceSet? {
        return getRelatedSetList()?.get(name)
    }

    override fun getRelatedSetList(): MutableMap<String, RestResourceSet>? {
        mRelatedSets?.let {
            mRelatedSets = hashMapOf()
        }
        return mRelatedSets
    }

    protected open fun clearRelatedSetList() {
        synchronized(this) {
            this.mRelatedSets?.clear()
            this.mRelatedSets = null
        }
    }

    override fun toString(): String {
        return getName() + " - " + getUniqueColumn() + ": " + mUniqueID + (if (isHidden()) ", hidden" else "") + if (isReadOnly()) ", readonly" else ""
    }

    override fun equals(o: Any?): Boolean {
        return o is RestResource && o.getMeta()!! == getMeta()
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}