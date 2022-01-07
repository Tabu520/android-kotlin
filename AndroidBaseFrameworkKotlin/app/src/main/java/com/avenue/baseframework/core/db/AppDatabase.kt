package com.avenue.baseframework.core.db

import androidx.lifecycle.MutableLiveData
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.avenue.baseframework.core.db.converter.DateConverter
import com.avenue.baseframework.core.db.dao.LoginInfoDao
import com.avenue.baseframework.core.db.dao.XMLTemplateDao
import com.avenue.baseframework.core.db.entity.LSResultEntity
import com.avenue.baseframework.core.db.entity.LoginInfoEntity
import com.avenue.baseframework.core.db.entity.XMLTemplateEntity

@Database(
    entities = [LSResultEntity::class, XMLTemplateEntity::class, LoginInfoEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class AppDatabase: RoomDatabase() {

    abstract fun xmlTemplateDao(): XMLTemplateDao

    abstract fun loginInfoDao(): LoginInfoDao

    companion object {
        var mIsDatabaseCreated = MutableLiveData<Boolean>()
        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                //database.execSQL("ALTER TABLE OPL_LB_HANDOVER ADD COLUMN DEVICEID TEXT");
            }
        }
    }
}