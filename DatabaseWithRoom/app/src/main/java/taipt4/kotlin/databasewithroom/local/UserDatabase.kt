package taipt4.kotlin.databasewithroom.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import taipt4.kotlin.databasewithroom.local.UserDatabase.Companion.DATABASE_VERSION
import taipt4.kotlin.databasewithroom.model.User

@Database(entities = arrayOf(User::class), version = DATABASE_VERSION)
abstract class UserDatabase: RoomDatabase() {
    abstract fun userDAO(): UserDAO

    companion object {
        const val DATABASE_VERSION = 1
        val DATABASE_NAME = "TaiPT4-Database-Room"

        private var sInstance: UserDatabase? = null
        fun getInstance(context: Context): UserDatabase {
            if (sInstance == null) {
                sInstance = Room.databaseBuilder(context, UserDatabase::class.java, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return sInstance!!
        }
    }


}