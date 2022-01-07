package com.avenue.baseframework.restclient.mbo

import com.avenue.baseframework.restclient.attribute.RestAttribute
import com.avenue.baseframework.restclient.resource.RestResource
import com.avenue.baseframework.restclient.resource.RestResourceImpl
import com.avenue.baseframework.restclient.resourceset.RestResourceSet

class RestMbo: RestResourceImpl {

    constructor(restMboSet: RestMboSet): super(restMboSet)

    constructor(index: Int, restMboSet: RestMboSet): super(index, restMboSet)

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

    override fun getHandlerContext(): String {
        return "mbo"
    }

    override fun getRelatedSet(name: String?): RestResourceSet {
        var retSet = getRelatedSetList()!![name!!]
        if (retSet == null) {
            retSet = RestMboSet(null, getMaximoRestConnector()!!)
            retSet.owner = this
            retSet.relationshipName = name
            getRelatedSetList()!![name] = retSet
        }
        return retSet
    }

    override fun getURI(): String? {
        getOwner()?.let {
            return this.mThisSet!!.getURI().toString() + "/" + this.mUniqueID
        }
        return super.getURI()
    }

    fun getOwner(): RestResource? {
        mThisSet?.let {
            return (mThisSet as RestMboSet).owner
        }
        return null
    }
}