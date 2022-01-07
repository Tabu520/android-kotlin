package com.avenue.baseframework.restclient.resourceset

import com.avenue.baseframework.restclient.resource.RestResource
import com.avenue.baseframework.restclient.resource.RestResourceImpl
import com.avenue.baseframework.restclient.resource.RestResourceMeta
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
import java.nio.charset.StandardCharsets
import java.util.*

abstract class RestResourceSetImpl : RestResourceSet {

    init {
        maxItems = 0
        pageID = 1
        whereClause = null
        selectClause = ""
        orderByAscClause = null
        orderByDescClause = null
        restQueryWhere = null
        isLoaded = false
        rsStart = 0
        rsCount = 0
        rsTotal = 0
        currRes = null
        currIndex = -1
        toBeSaved = false
    }

    constructor() {
        this.setMemberList(ArrayList<RestResource>())
    }

    constructor(name: String, connector: MaximoRestConnector) : this() {
        this.setName(name)
        this.setMaximoRestConnector(connector)
    }

    constructor(name: String, tableName: String, connector: MaximoRestConnector) : this(name, connector) {
        this.setTableName(tableName)
    }

    protected abstract fun initNewResource(): RestResource?

    override fun setMaximoRestConnector(maximoRestConnector: MaximoRestConnector?): RestResourceSet? {
        mc = maximoRestConnector
        mc?.let {
            val options: RestOptions = it.restOptions!!
            if (options.handlerContext == null) {
                options.handlerContext = getHandlerContext()
            }
        }
        return this
    }

    override fun getMaximoRestConnector(): MaximoRestConnector? {
        return mc
    }

    protected open fun initMeta(): RestResourceMeta? {
        return RestResourceMeta()
    }

    protected open fun setMeta(meta: RestResourceMeta?): RestResourceSet? {
        this.meta = meta
        return this
    }

    open fun getResourceMeta(): RestResourceMeta? {
        if (meta == null) {
            setMeta(initMeta())
        }
        return meta
    }

    protected open fun setLoaded(value: Boolean): RestResourceSet? {
        isLoaded = value
        return this
    }

    override fun setName(name: String?): RestResourceSet? {
        this.meta!!.name = name?.uppercase(Locale.getDefault())
        return this
    }

    override fun getName(): String? {
        return this.meta!!.name
    }

    override fun setTableName(tableName: String?): RestResourceSet? {
        this.meta!!.tableName = tableName?.uppercase(Locale.getDefault())
        return this
    }

    override fun getTableName(): String? {
        return this.meta!!.tableName
    }

    override fun toBeSaved(): Boolean {
        return if (toBeSaved) {
            true
        } else {
            var rs = moveFirst()
            while (rs != null) {
                if (rs.toBeSaved()) {
                    this.toBeSaved = true
                    return toBeSaved
                }
                rs = moveNext()
            }
            false
        }
    }

    override fun getURI(): String? {
        return getMaximoRestConnector()!!.getConnectorURI(this).toString() + "/" + getName()
    }

    override fun setStartPosition(startPosition: Int): RestResourceSet? {
        rsStart = startPosition
        return this
    }

    override fun setUniqueColumn(value: String?): RestResourceSet? {
        this.meta!!.setUniqueColumn(value)
        return this
    }

    override fun getUniqueColumn(): String? {
        return this.meta!!.getUniqueColumn()
    }

    protected open fun onResponseHandled() {
        setLoaded(true)
    }

    protected open fun setMemberList(list: MutableList<RestResource>?): RestResourceSet? {
        memberList = list
        return this
    }

    override fun <T : RestResource?> getMemberList(subTypeOfResource: Class<T>?): MutableList<T> {
        val retList = ArrayList<T>()
        val var3: Iterator<*> = memberList!!.iterator()
        while (var3.hasNext()) {
            val rs = var3.next() as RestResource
            retList.add(rs.mIndex, subTypeOfResource!!.cast(rs)!!)
        }
        return retList
    }

    override fun select(vararg attributes: String): RestResourceSet? {
        selectClause = RestQuerySelect().select(*attributes)
        return this
    }

    override fun where(clause: String?): RestResourceSet? {
        whereClause = clause
        return this
    }

    override fun where(restQueryWhere: RestQueryWhere?): RestResourceSet? {
        this.restQueryWhere = restQueryWhere
        return this
    }

    override fun orderByAsc(vararg attributes: String): RestResourceSet? {
        orderByAscClause = RestQuerySelect().select(*attributes)
        return this
    }

    override fun orderByDesc(vararg attributes: String): RestResourceSet? {
        orderByDescClause = RestQuerySelect().select(*attributes)
        return this
    }

    override fun setMaxItems(size: Int): RestResourceSet? {
        maxItems = size
        return this
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
        } catch (var12: Exception) {
            throw RestException("Errors while parsing the response", var12)
        } finally {
            try {
                response!!.close()
                disconnect()
            } catch (var11: IOException) {
                throw RestException("Errors while closing the response", var11)
            }
        }
    }

    override fun parseJsonResponse(parser: Any?) {
        try {
            val jsonParser = parser as JsonParser
            var token = jsonParser.currentToken
            var fieldName: String? = ""
            label56@ while (token != JsonToken.END_OBJECT) {
                if (token == JsonToken.FIELD_NAME) {
                    fieldName = jsonParser.currentName
                } else if (token == JsonToken.VALUE_NUMBER_INT) {
                    when (fieldName) {
                        "rsStart" -> setStartPosition(jsonParser.intValue)
                        "rsCount" -> this.rsCount = jsonParser.intValue
                        "rsTotal" -> this.rsTotal = jsonParser.intValue
                    }
                } else if (token == JsonToken.START_ARRAY) {
                    if (getName() == null) {
                        setName(fieldName)
                    }
                    if (getTableName() == null) {
                        setTableName(fieldName)
                    }
                    var token2 = jsonParser.currentToken
                    while (true) {
                        if (token2 == JsonToken.END_ARRAY || token2 == null) {
                            break@label56
                        }
                        if (token2 == JsonToken.START_OBJECT) {
                            val newRes: RestResource = this.populateNewResource()!!
                            newRes.parseJsonResponse(parser)
                            this.onResourceFetched(newRes)
                        }
                        token2 = jsonParser.nextToken()
                    }
                }
                token = jsonParser.nextToken()
            }
            onResponseHandled()
        } catch (var7: IOException) {
            throw RestException(var7)
        }
    }

    override fun parseXMLResponse(parser: Any?) {
        try {
            val xmlParser = parser as XmlPullParser
            var event = xmlParser.eventType
            while (event != 1) {
                var requiredMeta: String? = null
                var hiddenMeta: String? = null
                var readonlyMeta: String? = null
                var tagName = xmlParser.name
                if (event == 2) {
                    val rsStart = xmlParser.getAttributeValue(null, "rsStart")
                    val rsCount = xmlParser.getAttributeValue(null, "rsCount")
                    val rsTotal = xmlParser.getAttributeValue(null, "rsTotal")
                    requiredMeta = xmlParser.getAttributeValue(null, "required")
                    hiddenMeta = xmlParser.getAttributeValue(null, "hidden")
                    readonlyMeta = xmlParser.getAttributeValue(null, "readonly")
                    tagName = xmlParser.name
                    if (rsStart == null && rsCount == null && rsTotal == null) {
                        if (requiredMeta == null && hiddenMeta != null && readonlyMeta != null) {
                            if (getTableName() != null && tagName != getTableName()) {
                                break
                            }
                            if (getTableName() == null) {
                                setTableName(tagName)
                            }
                            val newRes: RestResource = this.populateNewResource()!!
                            newRes.parseXMLResponse(xmlParser)
                            this.onResourceFetched(newRes)
                        }
                    } else {
                        rsStart?.let {
                            setStartPosition(it.toInt())
                        }
                        rsCount?.let {
                            this.rsCount = it.toInt()
                        }
                        rsTotal?.let {
                            this.rsTotal = it.toInt()
                        }
                    }
                }
                if (event == 3 && (tagName == getTableName() || requiredMeta == null && hiddenMeta == null && readonlyMeta == null)) {
                    break
                }
                event = xmlParser.next()
            }
            onResponseHandled()
        } catch (var12: XmlPullParserException) {
            throw RestException(var12)
        } catch (var12: IOException) {
            throw RestException(var12)
        }
    }

    protected open fun onResourceFetched(resource: RestResource) {
        if (getUniqueColumn() == null) {
            setUniqueColumn(resource.getUniqueColumn())
        }
    }

    @Throws(RestException::class)
    override fun loadFromServer() {
        if (!this.isLoaded) {
            try {
                this.cleanOldResourceSetParams()
                this.buildParams()
                getMaximoRestConnector()!![getURI(), this]
            } catch (var2: IOException) {
                throw RestException(var2.message!!, var2)
            }
        } else {
            throw RestException("$this is already loaded. Please use reloadFromServer()")
        }
    }

    @Throws(RestException::class)
    override fun reloadFromServer() {
        this.clear()
        setLoaded(false)
        loadFromServer()
    }

    override fun isPagingAvailable(): Boolean {
        return maxItems > 0
    }

    override fun hasNextPage(): Boolean {
        return isPagingAvailable() && rsStart + rsCount < rsTotal
    }

    @Throws(RestException::class)
    override fun nextPage(): RestResourceSet? {
        return if (isPagingAvailable()) {
//            val tc = if (this.getRsTotal() != null) this.getRsTotal() else totalCount()
            val tc = if (rsTotal == 0) rsTotal else totalCount()
            val ns: Int = this.count() + maxItems
            setStartPosition(if (ns < tc) ns else this.count())
            pageID.inc()
            this
        } else {
            throw RestException("Paging is not available")
        }
    }

    @Throws(RestException::class)
    override fun previousPage(): RestResourceSet? {
        return if (isPagingAvailable()) {
            val ns: Int = rsStart - maxItems
            setStartPosition(ns.coerceAtLeast(0))
            pageID.dec()
            this
        } else {
            throw RestException("Paging is not available")
        }
    }

    @Throws(RestException::class)
    protected open fun buildParams() {
        try {
            val params: RestParams = getMaximoRestConnector()!!.restParams!!
            params.put("_rsStart", rsStart)
            params.put("_maxItems", maxItems)
            if (selectClause != null && selectClause!!.isNotEmpty()) {
                params.put("_includecols", selectClause)
            }
            orderByAscClause?.let {
                params.put("_orderbyasc", it)
            }
            orderByDescClause?.let {
                params.put("_orderbydesc", it)
            }
            whereClause?.let {
                params.put("_uw", it)
            }
            restQueryWhere?.let {
                params.put("#restquerywhere", it.whereClause())
            }
        } catch (var2: Exception) {
            throw RestException("Errors while building connection parameters", var2)
        }
    }

    private fun cleanOldResourceSetParams() {
        val connector = getMaximoRestConnector()
        if (connector != null) {
            val restParams: RestParams = connector.restParams!!
            restParams.let {
                it.remove("_maxItems")
                it.remove("_includecols")
                it.remove("_orderbyasc")
                it.remove("_orderbydesc")
                it.remove("_uw")
                it.remove("#restquerywhere")
            }
        }
    }

    override fun isEmpty(): Boolean {
        return this.count() <= 0
    }

    override fun count(): Int {
        memberList?.let {
            return it.size
        }
        return 0
    }

    override fun countAfterCommitToServer(): Int {
        var count = this.count()
        val var2: Iterator<*> = this.memberList!!.iterator()
        while (var2.hasNext()) {
            val rs = var2.next() as RestResource
            if (rs.toBeDeleted()) {
                --count
            }
        }
        return count
    }

    override fun totalCount(): Int {
        return rsStart + this.count()
    }

    override fun get(index: Int): RestResource? {
        return if (index >= 0 && index < this.count()) this.memberList!![index] else null
    }

    override fun get(uniqueId: String?): RestResource? {
        memberList?.let {
            if (isLoaded && it.isNotEmpty()) {
                val var2: Iterator<*> = it.iterator()
                while (var2.hasNext()) {
                    val rs = var2.next() as RestResource
                    if (rs.mUniqueID.equals(uniqueId)) {
                        return rs
                    }
                }
            }
        }
        return null
    }

    protected open fun populateNewResource(): RestResource? {
        val newRes = initNewResource()?.setIndex(this.count())
        newRes?.let {
            this.memberList!!.add(this.count(), newRes)
            return newRes
        }
        return null
    }

    override fun add(): RestResource? {
        val newRes = populateNewResource()
        newRes?.let {
            it.setToBeAdded(true)
            (it as RestResourceImpl?)!!.onAdded()
            return it
        }
        return null
    }

    override fun remove(restResource: RestResource?): RestResource? {
        return this.remove(restResource?.mUniqueID)
    }

    override fun remove(index: Int): RestResource? {
        memberList?.let {
            if (it.isNotEmpty()) {
                val removed = it.removeAt(index)
                removed.setToBeDeleted(true)
                this.updateIndicesForResourcesFrom(removed.mIndex)
                return removed
            }
        }
        return null
    }

    override fun remove(uniqueId: String?): RestResource? {
        val toBeRemovedRes = this[uniqueId]
        if (toBeRemovedRes != null) {
            this.remove(toBeRemovedRes.mIndex)
        }
        return null
    }

    private fun updateIndicesForResourcesFrom(fromPos: Int) {
        memberList?.let {
            for (i in fromPos until it.size) {
                this[i]!!.setIndex(i)
            }
        }
    }

    @Throws(RestException::class, IOException::class)
    protected open fun reloadToIncludeUniqueColumn() {
        val uniqueColumn = getUniqueColumn()
        val selectClause: String = this.selectClause!!
        if (!selectClause.contains(uniqueColumn!!)) {
            select(uniqueColumn, selectClause)
            reloadFromServer()
        }
    }

    @Throws(RestException::class)
    override fun saveToServer() {
        if (canSaveToServer()) {
            validateForServer()
            cleanOldResourceSetParams()
            buildParams()
            var rs = moveFirst()
            while (rs != null) {
                rs.saveToServer()
                rs = moveNext()
            }
            disconnect()
        }
    }

    override fun discardChanges(): RestResourceSet? {
        var rs = moveFirst()
        while (rs != null) {
            rs.discardChanges()
            rs = moveNext()
        }
        return this
    }

    override fun clear() {
        synchronized(this) {
            memberList?.let {
                var rs = moveLast()
                while (rs != null) {
                    rs.close()
                    rs = movePrevious()
                }
                it.clear()
            }
        }
        this.onClear()
    }

    protected open fun onClear() {}

    override fun close() {
        this.clear()
        synchronized(this) {
            setMeta(null)
            maxItems = 0
            whereClause = null
            selectClause = null
            orderByAscClause = null
            orderByDescClause = null
            restQueryWhere = null
            mc = null
        }
        onClosed()
    }

    protected open fun onClosed() {}

    @Throws(RestException::class)
    override fun disconnect() {
        try {
            getMaximoRestConnector()!!.disconnect()
            getMaximoRestConnector()!!.closeConnection()
        } catch (var2: IOException) {
            throw RestException(var2.message!!, var2)
        }
    }

    protected open fun setCurrentResource(res: RestResource?) {
        currRes = res
    }

    override fun getCurrentResource(): RestResource? {
        return currRes
    }

    override fun moveFirst(): RestResource? {
        return moveTo(0)
    }

    override fun moveLast(): RestResource? {
        return moveTo(this.count() - 1.also { currIndex = it })
    }

    override fun moveNext(): RestResource? {
        return moveTo(currIndex + 1)
    }

    override fun movePrevious(): RestResource? {
        return moveTo(currIndex - 1)
    }

    override fun moveTo(position: Int): RestResource? {
        val res = this[position]
        if (res != null) {
            currIndex = position
            setCurrentResource(res)
        }
        return res
    }

    override fun canSaveToServer(): Boolean {
        return true
    }

    @Throws(RestException::class)
    override fun validateForServer() {
        if (meta == null || getName() == null || getName()!!.isEmpty()) {
            throw RestException("The resource set is not valid")
        }
    }

    override fun toString(): String {
        return meta.toString() + " - RsStart: " + rsStart + ", RsTotal: " + rsTotal + ", RsCount: " + rsCount + ", Count: " + count()
    }

    override fun equals(other: Any?): Boolean {
        return other is RestResourceSet && other.meta!! == this.meta
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}