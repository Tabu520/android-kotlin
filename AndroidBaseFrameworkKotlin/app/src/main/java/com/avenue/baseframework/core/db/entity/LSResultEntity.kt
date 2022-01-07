package com.avenue.baseframework.core.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.avenue.baseframework.core.helpers.EString
import com.avenue.baseframework.core.helpers.TemplateConfig
import com.avenue.baseframework.core.utils.DateTimeUtils
import com.avenue.baseframework.restclient.utils.RestParams
import java.util.*

@Entity(tableName = "OPL_LS_RESULT")
data class LSResultEntity(
    @PrimaryKey(autoGenerate = true)
    var OPL_LS_RESULTID: Long = 0,
    var OPL_LS_RESULTIDM: Long = 0,
    var OPL_LS_MAINID: Long = 0,
    var TEMPLATEID: String? = "",
    var TEMPLATENAME: String? = "",
    var GROUPTYPE: String? = "",
    var SECTIONID: String? = "",
    var SECTIONNAME: String? = "",
    var CHILDSECID: String? = "",
    var CHILDSECNAME: String? = "",
    var CONTROLID: String? = "",
    var CONTROLNAME: String? = "",
    var LINEGROUP: String? = "",
    var TAGID: String? = "",
    var VALUE: String? = "",
    var INPUTDATE: Date? = null,
    var USERID: String? = "",
    var ABNORMAL: Boolean? = false,
    var LATITUDE: String? = "",
    var LONGITUDE: String? = "",
    var INPUTTIME: String? = "",
    var CONTROLTYPE: String? = "",
    var DEVICEID: String? = "",
    var SYNC_TYPE: String? = ""
) : BaseEntity() {

    override fun getRestParams(): RestParams {
        val restParams: RestParams = RestParams.getDefault()
        restParams.put(Cols.OPL_LS_MAINID, OPL_LS_MAINID)
        restParams.put(Cols.TEMPLATEID, TEMPLATEID)
        restParams.put(Cols.TEMPLATENAME, TEMPLATENAME)
        restParams.put(Cols.GROUPTYPE, GROUPTYPE)
        restParams.put(Cols.SECTIONID, SECTIONID)
        restParams.put(Cols.SECTIONNAME, SECTIONNAME)
        restParams.put(Cols.CHILDSECID, CHILDSECID)
        restParams.put(Cols.CHILDSECNAME, CHILDSECNAME)
        restParams.put(Cols.CONTROLID, CONTROLID)
        restParams.put(Cols.CONTROLNAME, CONTROLNAME)
        restParams.put(Cols.VALUE, VALUE)
        restParams.put(Cols.USERID, USERID)
        restParams.put(Cols.ABNORMAL, ABNORMAL)
        restParams.put(Cols.LATITUDE, LATITUDE)
        restParams.put(Cols.LONGITUDE, LONGITUDE)
        restParams.put(Cols.LINEGROUP, LINEGROUP)
        restParams.put(Cols.TAGID, TAGID)
        restParams.put(Cols.INPUTTIME, INPUTTIME)
        restParams.put(Cols.CONTROLTYPE, CONTROLTYPE)
        restParams.put(Cols.DEVICEID, DEVICEID)
        if (INPUTDATE != null) {
            restParams.put(
                Cols.INPUTDATE,
                DateTimeUtils.formatWithMaximoPattern(INPUTDATE, TemplateConfig.DATE_FORMAT)
            )
        } else {
            restParams.put(Cols.INPUTDATE, INPUTDATE)
        }

        restParams.put(Cols.LOCAL_LS_RESULTID, OPL_LS_RESULTID)
        restParams.put(
            Cols.OPL_LS_RESULTIDM,
            if (OPL_LS_RESULTIDM == 0L) EString.EMPTY else OPL_LS_RESULTIDM
        )
        restParams.put(Cols.SYNC_TYPE, SYNC_TYPE)
        return restParams
    }

    override fun getEntityRestful(): String {
        return "OPL_LS_RESULT"
    }

    override fun getInsertRestful(): String {
        return "OPL_LS_RESULT?" + getRestParams().getParamsString()
    }

    override fun getUpdateRestful(): String {
        return "OPL_LS_RESULT/" + OPL_LS_RESULTIDM + "?" + getRestParams().getParamsString()
    }

    override fun getDeleteRestful(): String {
        return "OPL_LS_RESULT/$OPL_LS_RESULTIDM"
    }

    override fun getInsertCsvRestful(): String {
        return "OPL_LS_RESULT?" + "action=importfile&lean=1"
    }

    override fun getInsertCsvBody(): String {
        return getRestParams().getCsvValueParamsString()
    }

    override fun getInsertCsvHeader(): String {
        return getRestParams().getCsvHeaderParamsString()
    }

    override fun getUpdateCsvBody(): String {
        val restParams = getRestParams()
        restParams.put(Cols.OPL_LS_RESULTIDM, OPL_LS_RESULTIDM)
        return restParams.getCsvValueParamsString()
    }

    override fun getSelectOslcRestful(localIDs: String?, idM: String?): String? {
        return "OPL_LS_RESULT?" + String.format(
            "gbcols=local_opl_ls_resultid,max.opl_ls_resultid&oslc.where=local_opl_ls_resultid%sopl_ls_mainid=%s&oslc.groupby=opl_ls_resultid",
            localIDs,
            idM
        )
    }

    object Cols {
        const val OPL_LS_MAINID = "OPL_LS_MAINID"
        const val TEMPLATEID = "TEMPLATEID"
        const val TEMPLATENAME = "TEMPLATENAME"
        const val GROUPTYPE = "GROUPTYPE"
        const val SECTIONID = "SECTIONID"
        const val SECTIONNAME = "SECTIONNAME"
        const val CHILDSECID = "CHILDSECID"
        const val CHILDSECNAME = "CHILDSECNAME"
        const val CONTROLID = "CONTROLID"
        const val CONTROLNAME = "CONTROLNAME"
        const val VALUE = "VALUE"
        const val INPUTDATE = "INPUTDATE"
        const val USERID = "USERID"
        const val ABNORMAL = "ABNORMAL"
        const val LATITUDE = "LATITUDE"
        const val LONGITUDE = "LONGITUDE"
        const val LINEGROUP = "LINEGROUP"
        const val TAGID = "TAGID"
        const val INPUTTIME = "INPUTTIME"
        const val CONTROLTYPE = "CONTROLTYPE"
        const val DEVICEID = "DEVICEID"
        const val LOCAL_LS_RESULTID = "LOCAL_OPL_LS_RESULTID"
        const val OPL_LS_RESULTIDM = "OPL_LS_RESULTID" // IDMaximo
        const val SYNC_TYPE = "SYNC_TYPE"
    }

}
