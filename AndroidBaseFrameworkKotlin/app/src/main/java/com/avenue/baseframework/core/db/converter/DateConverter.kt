package com.avenue.baseframework.core.db.converter

import androidx.room.TypeConverter
import java.util.*

class DateConverter {

    @TypeConverter
    fun toDate(timestamp: Long?): Date? {
        timestamp?.let {
            return Date(timestamp)
        }
        return null
    }

    @TypeConverter
    fun toTimestamp(date: Date?): Long? {
        return date?.time
    }
}