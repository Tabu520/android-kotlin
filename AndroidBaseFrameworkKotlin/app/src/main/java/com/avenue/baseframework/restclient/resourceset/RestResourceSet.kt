package com.avenue.baseframework.restclient.resourceset

import com.avenue.baseframework.restclient.resource.RestResource
import com.avenue.baseframework.restclient.resource.RestResourceMeta
import com.avenue.baseframework.restclient.utils.*

interface RestResourceSet: RestRequester, RestResponseParser, RestEntity {

    var meta: RestResourceMeta?
    var maxItems: Int
    var pageID: Int
    var whereClause: String?
    var selectClause: String?
    var orderByAscClause: String?
    var orderByDescClause: String?
    var restQueryWhere: RestQueryWhere?
    var mc: MaximoRestConnector?
    var memberList: MutableList<RestResource>?
    var isLoaded: Boolean
    var rsStart: Int
    var rsCount: Int
    var rsTotal: Int
    var currRes: RestResource?
    var currIndex: Int
    var toBeSaved: Boolean

    fun setMaximoRestConnector(maximoRestConnector: MaximoRestConnector?): RestResourceSet?

    fun getMaximoRestConnector(): MaximoRestConnector?

    fun toBeSaved(): Boolean

    fun setTableName(tableName: String?): RestResourceSet?

    fun getTableName(): String?

    fun setName(name: String?): RestResourceSet?

    fun getName(): String?

    fun setStartPosition(startPosition: Int): RestResourceSet?

    fun setUniqueColumn(value: String?): RestResourceSet?

    fun getUniqueColumn(): String?

    fun <T : RestResource?> getMemberList(subTypeOfResource: Class<T>?): MutableList<T>

    fun select(vararg attributes: String): RestResourceSet?

    fun where(clause: String?): RestResourceSet?

    fun where(restQueryWhere: RestQueryWhere?): RestResourceSet?

    fun orderByAsc(vararg attributes: String): RestResourceSet?

    fun orderByDesc(vararg attributes: String): RestResourceSet?

    fun setMaxItems(size: Int): RestResourceSet?

    fun isPagingAvailable(): Boolean

    fun hasNextPage(): Boolean

    @Throws(RestException::class)
    fun nextPage(): RestResourceSet?

    @Throws(RestException::class)
    fun previousPage(): RestResourceSet?

    fun getCurrentResource(): RestResource?

    fun moveFirst(): RestResource?

    fun moveLast(): RestResource?

    fun moveNext(): RestResource?

    fun movePrevious(): RestResource?

    fun moveTo(position: Int): RestResource?

    fun isEmpty(): Boolean

    fun count(): Int

    fun countAfterCommitToServer(): Int

    fun totalCount(): Int

    operator fun get(index: Int): RestResource?

    operator fun get(uniqueId: String?): RestResource?

    fun add(): RestResource?

    fun remove(restResource: RestResource?): RestResource?

    fun remove(uniqueId: String?): RestResource?

    fun remove(index: Int): RestResource?

    fun discardChanges(): RestResourceSet?

    fun clear()
}