package com.avenue.baseframework.core.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "LOGIN_INFO")
data class LoginInfoEntity(
    var USER_NAME: String = "",
    var USER_PASSWORD: String = ""
) {

    @PrimaryKey(autoGenerate = true)
    var USER_ID = 0
}