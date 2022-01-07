package com.avenue.baseframework.restclient.mbo

import com.avenue.baseframework.restclient.resource.RestResource
import com.avenue.baseframework.restclient.resource.RestResourceMeta
import com.avenue.baseframework.restclient.resourceset.RestResourceSet
import com.avenue.baseframework.restclient.resourceset.RestResourceSetImpl
import com.avenue.baseframework.restclient.utils.MaximoRestConnector
import com.avenue.baseframework.restclient.utils.RestQueryWhere

class RestMboSet(
    resName: String?,
    mc: MaximoRestConnector
) : RestResourceSetImpl(resName!!, mc) {

    var relationshipName: String? = null
    var owner: RestResource? = null

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


    override fun initNewResource(): RestResource {
        return RestMbo(this)
    }

    override fun getHandlerContext(): String {
        return "mbo"
    }

    override fun getURI(): String? {
        val strb = StringBuilder(getMaximoRestConnector()!!.getConnectorURI(this)!!)
        owner?.let {
            if (it is RestMbo && !it.toBeAdded() && !it.toBeDeleted()) {
                strb.append("/").append(it.getTableName()).append("/").append(it.mUniqueID)
            }
        }
        strb.append("/")
        if (relationshipName != null) {
            strb.append(this.relationshipName)
        } else {
            strb.append(getName())
        }
        return strb.toString()
    }

    override fun setName(name: String?): RestResourceSet? {
        setTableName(name)
        return super.setName(name)
    }
}