package com.avenue.baseframework.core.models.iface

interface XMLTemplate {

    fun getOPL_XML_TEMPLATEID(): Long

    fun getORGID(): String?

    fun getSITEID(): String?

    fun getZONEID(): String?

    fun getAREAID(): String?

    fun getLOCATION_INFO(): String?

    fun getTEMPLATEID(): String?

    fun getTEMPLATE_DESCRIPTION(): String?

    fun getPATH(): String?

    fun getLOCAL_PATH(): String?

    fun getFREQUENCY(): String?

    fun getVERSION(): String?

    fun getSYNC_TYPE(): String?
}