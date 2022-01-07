package com.avenue.baseframework.core.models.iface

import java.util.*

interface LSResult {

    fun getOPL_LS_RESULTID(): Long

    fun getOPL_LS_RESULTIDM(): Long

    fun getOPL_LS_MAINID(): Long

    fun getTEMPLATEID(): String?

    fun getTEMPLATENAME(): String?

    fun getGROUPTYPE(): String?

    fun getSECTIONID(): String?

    fun getSECTIONNAME(): String?

    fun getCHILDSECID(): String?

    fun getCHILDSECNAME(): String?

    fun getCONTROLID(): String?

    fun getCONTROLNAME(): String?

    fun getVALUE(): String?

    fun getINPUTDATE(): Date?

    fun getUSERID(): String?

    fun getABNORMAL(): Boolean?

    fun getLATITUDE(): String?

    fun getLONGITUDE(): String?

    fun getLINEGROUP(): String?

    fun getSYNC_TYPE(): String?

    fun getTAGID(): String?

    fun getINPUTTIME(): String?

    fun getDEVICEID(): String?

    fun getCONTROLTYPE(): String?
}