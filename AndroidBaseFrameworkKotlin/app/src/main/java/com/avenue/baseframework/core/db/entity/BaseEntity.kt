package com.avenue.baseframework.core.db.entity

import com.avenue.baseframework.restclient.utils.RestParams

abstract class BaseEntity {

    abstract fun getRestParams(): RestParams?

    abstract fun getEntityRestful(): String?

    abstract fun getInsertRestful(): String?

    abstract fun getUpdateRestful(): String?

    abstract fun getDeleteRestful(): String?

    abstract fun getInsertCsvRestful(): String?

    abstract fun getInsertCsvBody(): String?

    abstract fun getInsertCsvHeader(): String?

    abstract fun getUpdateCsvBody(): String?

    abstract fun getSelectOslcRestful(localIDs: String?, idM: String?): String?
}