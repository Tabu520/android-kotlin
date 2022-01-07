package com.avenue.baseframework.core.repository

import androidx.lifecycle.MediatorLiveData
import com.avenue.baseframework.core.db.AppDatabase
import com.avenue.baseframework.core.db.dao.LoginInfoDao
import com.avenue.baseframework.core.db.dao.XMLTemplateDao
import com.avenue.baseframework.core.db.entity.LoginInfoEntity
import com.avenue.baseframework.core.db.entity.XMLTemplateEntity
import javax.inject.Inject

class DataRepository @Inject constructor(
    private val appDatabase: AppDatabase
) {

    @Inject
    lateinit var loginInfoDao: LoginInfoDao

    @Inject
    lateinit var xmlTemplateDao: XMLTemplateDao

    var mObservableLSTemplatesList: MediatorLiveData<List<XMLTemplateEntity>> = MediatorLiveData()

    init {
        mObservableLSTemplatesList.addSource(
            xmlTemplateDao.loadLSXMLTemplate()
        ) { templateEntities ->
            if (AppDatabase.mIsDatabaseCreated.value != null) {
                mObservableLSTemplatesList.postValue(templateEntities)
            }
        }
    }

    // ----- Login Info ---- //

    fun loadAllLoginInfoData() = loginInfoDao.loadLoginInfoList()

    suspend fun insertAllLoginInfo(loginInfoList: List<LoginInfoEntity>) = loginInfoDao.insertAll(loginInfoList)

    suspend fun updateAllLoginInfo(loginInfoList: List<LoginInfoEntity>) = loginInfoDao.updateAll(loginInfoList)

    suspend fun deleteAllLoginInfo(loginInfoList: List<LoginInfoEntity>) = loginInfoDao.deleteAll(loginInfoList)

    suspend fun updateUserPassword(userName: String, password: String) = loginInfoDao.updateUserPassword(userName, password)

    // ---- XML Template ---- //
    // All
    suspend fun insertXmlTemplate(xmlTemplates: List<XMLTemplateEntity>) = xmlTemplateDao.insertAll(xmlTemplates)
    suspend fun updateXmlTemplate(xmlTemplates: List<XMLTemplateEntity>) = xmlTemplateDao.updateAll(xmlTemplates)
    suspend fun deleteXmlTemplate(xmlTemplates: List<XMLTemplateEntity>) = xmlTemplateDao.deleteAll(xmlTemplates)
    suspend fun deleteAllXmlTemplate() = xmlTemplateDao.deleteAllXMLTemplate()
    fun loadAllXmlTemplate() = xmlTemplateDao.loadXMLTemplate()
    fun loadLBXmlTemplate() = xmlTemplateDao.loadLBXMLTemplate()
    fun loadLSXmlTemplate() = xmlTemplateDao.loadLSXMLTemplate()
    fun getXMLTemplateWithId(templateID: Long) = xmlTemplateDao.getXMLTemplateWithId(templateID)
    fun searchXMLTemplate(query: String) = xmlTemplateDao.searchXMLTemplate(query)

    // Logbook
    fun searchLBTemplate(zone: String, query: String) = xmlTemplateDao.searchLBTemplate(zone, query)
    fun searchLBTemplate(areas: Array<String>, zone: String, query: String) = xmlTemplateDao.searchLBTemplate(areas, zone, query)
    fun getLBXMLTemplate(zone: String) = xmlTemplateDao.getLBXMLTemplate(zone)
    fun getLBXMLTemplate(areas: Array<String>, zone: String) = xmlTemplateDao.getLBXMLTemplate(areas, zone)
    fun getLBXMLTemplateIn(zone: String) = xmlTemplateDao.getLBXMLTemplateIn(zone)

    // Log sheet
    fun searchLSTemplate(zone: String, query: String) = xmlTemplateDao.searchLSTemplate(zone, query)
    fun searchLSTemplate(areas: Array<String>, zone: String, query: String) = xmlTemplateDao.searchLSTemplate(areas, zone, query)
    fun getLSXMLTemplate(zone: String) = xmlTemplateDao.getLSXMLTemplate(zone)
    fun getLSXMLTemplate(areas: Array<String>, zone: String) = xmlTemplateDao.getLSXMLTemplate(areas, zone)

}