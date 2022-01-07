package com.avenue.baseframework.core.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.avenue.baseframework.core.db.entity.XMLTemplateEntity

@Dao
interface XMLTemplateDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(xmlTemplates: List<XMLTemplateEntity>): List<Long>

    @Update
    suspend fun updateAll(xmlTemplates: List<XMLTemplateEntity>): Int

    @Delete
    suspend fun deleteAll(xmlTemplates: List<XMLTemplateEntity>): Int

    @Query("DELETE FROM OPL_XML_TEMPLATE")
    suspend fun deleteAllXMLTemplate(): Int

    @Query("SELECT * FROM OPL_XML_TEMPLATE")
    fun loadXMLTemplate(): LiveData<List<XMLTemplateEntity>>

    @Query("select * from OPL_XML_TEMPLATE where TEMPLATEID = :TEMPLATEID")
    fun getXMLTemplateWithId(TEMPLATEID: Long): LiveData<XMLTemplateEntity>

    @Query("SELECT * FROM OPL_XML_TEMPLATE WHERE TEMPLATEID LIKE 'LB%'")
    fun loadLBXMLTemplate(): LiveData<List<XMLTemplateEntity>>

    @Query("SELECT * FROM OPL_XML_TEMPLATE WHERE TEMPLATEID LIKE 'LS%'")
    fun loadLSXMLTemplate(): LiveData<List<XMLTemplateEntity>>

    //LOGBOOK//
    @Query("SELECT * FROM OPL_XML_TEMPLATE WHERE TEMPLATE_DESCRIPTION LIKE :query")
    fun searchXMLTemplate(query: String): LiveData<List<XMLTemplateEntity>>

    @Query("SELECT * FROM OPL_XML_TEMPLATE WHERE TEMPLATEID LIKE 'LB%' AND ZONEID = :zone AND TEMPLATEID LIKE :query")
    fun searchLBTemplate(zone: String, query: String): LiveData<List<XMLTemplateEntity>>

    @Query("SELECT * FROM OPL_XML_TEMPLATE WHERE TEMPLATEID LIKE 'LB%' AND AREAID IN (:areas) AND ZONEID = :zone AND TEMPLATEID LIKE :query")
    fun searchLBTemplate(areas: Array<String>?, zone: String, query: String): LiveData<List<XMLTemplateEntity>>

    @Query("SELECT * FROM OPL_XML_TEMPLATE WHERE TEMPLATEID LIKE 'LB%' AND ZONEID = :zone  ORDER BY TEMPLATEID")
    fun getLBXMLTemplate(zone: String): LiveData<List<XMLTemplateEntity>>

    @Query("SELECT * FROM OPL_XML_TEMPLATE WHERE TEMPLATEID LIKE 'LB%' AND AREAID IN (:areas) AND ZONEID = :zone ORDER BY TEMPLATEID")
    fun getLBXMLTemplate(areas: Array<String>?, zone: String): LiveData<List<XMLTemplateEntity>>

    @Query("SELECT * FROM OPL_XML_TEMPLATE WHERE TEMPLATEID LIKE 'LB%' AND AREAID IN ('CNCN','ATVSV') AND ZONEID = :zone ORDER BY TEMPLATEID")
    fun getLBXMLTemplateIn(zone: String): LiveData<List<XMLTemplateEntity>>
    //LOGBOOK//

    //LOG SHEET
    @Query("SELECT * FROM OPL_XML_TEMPLATE WHERE TEMPLATEID LIKE 'LS%' AND  ZONEID = :zone AND TEMPLATEID LIKE :query ORDER BY TEMPLATEID")
    fun searchLSTemplate(zone: String, query: String): LiveData<List<XMLTemplateEntity>>

    @Query("SELECT * FROM OPL_XML_TEMPLATE WHERE TEMPLATEID LIKE 'LS%' AND AREAID IN (:areas) AND ZONEID = :zone AND  TEMPLATEID LIKE :query  ORDER BY TEMPLATEID")
    fun searchLSTemplate(areas: Array<String>?, zone: String, query: String): LiveData<List<XMLTemplateEntity>>

    @Query("SELECT * FROM OPL_XML_TEMPLATE WHERE TEMPLATEID LIKE 'LS%' AND ZONEID = :zone ORDER BY TEMPLATEID")
    fun getLSXMLTemplate(zone: String): LiveData<List<XMLTemplateEntity>>

    @Query("SELECT * FROM OPL_XML_TEMPLATE WHERE TEMPLATEID LIKE 'LS%' AND AREAID IN (:areas) AND ZONEID = :zone  ORDER BY TEMPLATEID")
    fun getLSXMLTemplate(areas: Array<String>?, zone: String): LiveData<List<XMLTemplateEntity>>
    //LOG SHEET
}