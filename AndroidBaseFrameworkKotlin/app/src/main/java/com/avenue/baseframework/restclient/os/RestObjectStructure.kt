package com.avenue.baseframework.restclient.os

import com.avenue.baseframework.restclient.attribute.RestAttribute
import com.avenue.baseframework.restclient.mbo.RestMboSet
import com.avenue.baseframework.restclient.resource.RestResource
import com.avenue.baseframework.restclient.resource.RestResourceImpl
import com.avenue.baseframework.restclient.resourceset.RestResourceSet
import com.avenue.baseframework.restclient.utils.RestException
import com.avenue.baseframework.restclient.utils.RestParams
import java.io.IOException

class RestObjectStructure: RestResourceImpl {

    val ACTION_CHANGE = "Change"
    val ACTION_ADD_CHANGE = "AddChange"

    override var mIndex: Int = 0
    override var mAttributeList: MutableList<RestAttribute>? = mutableListOf()
    override var mIsHidden: Boolean = false
    override var mIsReadOnly: Boolean = false
    override var mIsLoaded: Boolean = false
    override var mIsModified: Boolean = false
    override var mToBeAdded: Boolean = false
    override var mToBeDeleted: Boolean = false
    override var mThisSet: RestResourceSet? = null
    override var mUniqueID: String? = null
    override var mRowStamp: String? = null
    override var mRelatedSets: MutableMap<String, RestResourceSet>? = mutableMapOf()

    constructor(osSet: RestObjectStructureSet) : super(osSet)

    constructor(index: Int, osSet: RestObjectStructureSet) : super(index, osSet)


    @Throws(IOException::class, RestException::class)
    private fun lookupChildRelationship(childName: String): String? {
        var result: String? = null
        val mc = getMaximoRestConnector()
        val oldRestParams: RestParams = mc!!.restParams!!.clone()
        val moidSet: RestMboSet = RestMboSet("MAXINTOBJDETAIL", mc).select("RELATION")!!
            .where("INTOBJECTNAME='" + getName().toString() + "' and  OBJECTNAME ='" + childName + "'") as RestMboSet
        moidSet.loadFromServer()
        if (moidSet.count() > 0) {
            result = moidSet[0]!!.getString("RELATION")
            mc.setRestParams(oldRestParams)
        }
        return result
    }

    fun invokeAction(actionName: String?): RestResource {
        getMaximoRestConnector()!!.restParams!!.put("_action=", actionName)
        return this
    }

    override fun getHandlerContext(): String {
        return "os"
    }
}