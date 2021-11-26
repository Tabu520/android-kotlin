package com.taipt.roomdatabaseexample

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.taipt.roomdatabaseexample.entities.Director
import com.taipt.roomdatabaseexample.entities.School
import com.taipt.roomdatabaseexample.entities.Student
import com.taipt.roomdatabaseexample.entities.Subject
import com.taipt.roomdatabaseexample.entities.relations.StudentSubjectCrossRef

@Database(
    entities = [
        School::class,
        Student::class,
        Director::class,
        Subject::class,
        StudentSubjectCrossRef::class
    ],
    version = 1
)
abstract class SchoolDatabase : RoomDatabase() {

    abstract val schoolDao: SchoolDao

    companion object {
        @Volatile
        private var sInstance: SchoolDatabase? = null

        fun getInstance(context: Context): SchoolDatabase {
            synchronized(this) {
                return sInstance ?: Room.databaseBuilder(
                    context.applicationContext,
                    SchoolDatabase::class.java,
                    "school_db"
                ).build().also {
                    sInstance = it
                }
            }
        }
    }
}