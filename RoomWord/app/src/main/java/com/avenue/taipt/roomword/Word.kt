package com.avenue.taipt.roomword

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "word_table")
data class Word(
    @ColumnInfo(name = "word") var word: String
) {
    @PrimaryKey(autoGenerate = true) var id: Int? = null
}
