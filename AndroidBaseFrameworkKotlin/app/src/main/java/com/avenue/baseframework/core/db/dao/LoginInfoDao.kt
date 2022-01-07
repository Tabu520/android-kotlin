package com.avenue.baseframework.core.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.avenue.baseframework.core.db.entity.LoginInfoEntity

@Dao
interface LoginInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(loginInfoList: List<LoginInfoEntity>): List<Long>

    @Update
    suspend fun updateAll(loginInfoList: List<LoginInfoEntity>): Int

    @Delete
    suspend fun deleteAll(loginInfoList: List<LoginInfoEntity>): Int

    @Query("SELECT * FROM LOGIN_INFO")
    fun loadLoginInfoList(): LiveData<List<LoginInfoEntity>>

    @Query("UPDATE LOGIN_INFO SET USER_PASSWORD = :password WHERE USER_NAME = :username")
    suspend fun updateUserPassword(username: String, password: String)
}