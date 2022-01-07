package com.avenue.baseframework.restclient.os

import com.avenue.baseframework.restclient.mbo.RestMboSet
import com.avenue.baseframework.restclient.resource.RestResource
import com.avenue.baseframework.restclient.resource.RestResourceMeta
import com.avenue.baseframework.restclient.resourceset.RestResourceSetImpl
import com.avenue.baseframework.restclient.utils.MaximoRestConnector
import com.avenue.baseframework.restclient.utils.RestException
import com.avenue.baseframework.restclient.utils.RestQueryWhere
import java.io.IOException

class RestObjectStructureSet : RestResourceSetImpl {

    override var meta: RestResourceMeta? = null
    override var maxItems: Int = 0
    override var pageID: Int = 0
    override var whereClause: String? = null
    override var selectClause: String? = null
    override var orderByAscClause: String? = null
    override var orderByDescClause: String? = null
    override var restQueryWhere: RestQueryWhere? = null
    override var mc: MaximoRestConnector? = null
    override var memberList: MutableList<RestResource>? = null
    override var isLoaded: Boolean = false
    override var rsStart: Int = 0
    override var rsCount: Int = 0
    override var rsTotal: Int = 0
    override var currRes: RestResource? = null
    override var currIndex: Int = 0
    override var toBeSaved: Boolean = false

    constructor()

    constructor(resName: String, mc: MaximoRestConnector) : super(resName, mc)

    constructor(resName: String, tableName: String, mc: MaximoRestConnector) : super(
        resName,
        tableName,
        mc
    )

    override fun initNewResource(): RestResource {
        return RestObjectStructure(this)
    }

    override fun getHandlerContext(): String {
        return "os"
    }

    @Throws(RestException::class, IOException::class)
    fun getRootMboSet(): RestMboSet? {
        return if (getTableName() != null && getMaximoRestConnector() != null)
            RestMboSet(getTableName(), getMaximoRestConnector()!!)
                .setStartPosition(rsStart)!!
                .setMaxItems(maxItems)!!
                .select(selectClause!!)!!
                .where(whereClause)!!
                .where(restQueryWhere!!) as RestMboSet
        else null
    }
}