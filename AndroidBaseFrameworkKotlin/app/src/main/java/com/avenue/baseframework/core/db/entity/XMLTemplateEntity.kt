package com.avenue.baseframework.core.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.avenue.baseframework.core.helpers.EString
import com.avenue.baseframework.core.helpers.TemplateConfig

@Entity(tableName = "OPL_XML_TEMPLATE")
data class XMLTemplateEntity(
    @PrimaryKey
    var OPL_XML_TEMPLATEID: Long = 0,
    var ORGID: String? = EString.EMPTY,
    var SITEID: String? = EString.EMPTY,
    var ZONEID: String? = EString.EMPTY,
    var AREAID: String? = EString.EMPTY,
    var TEMPLATEID: String? = EString.EMPTY,
    var TEMPLATE_DESCRIPTION: String? = EString.EMPTY,
    var PATH: String? = EString.EMPTY,
    var LOCAL_PATH: String? = EString.EMPTY,
    var FREQUENCY: String? = EString.EMPTY,
    var VERSION: String? = EString.EMPTY,
    var SYNC_TYPE: String? = TemplateConfig.SYNC_NONE
) {
    fun getLOCATION_INFO(): String {
        return "Area: $AREAID -  Zone: $ZONEID"
    }
}