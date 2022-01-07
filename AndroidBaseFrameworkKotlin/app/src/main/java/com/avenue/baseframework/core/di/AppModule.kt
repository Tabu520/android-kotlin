package com.avenue.baseframework.core.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.avenue.baseframework.core.db.AppDatabase
import com.avenue.baseframework.core.helpers.AppSettings
import com.avenue.baseframework.core.helpers.AppSize
import com.avenue.baseframework.core.helpers.Constants
import com.avenue.baseframework.core.helpers.Constants.SHARED_PREFERENCES_NAME
import com.avenue.baseframework.core.models.UserSettings
import com.avenue.baseframework.restclient.RestConnector
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext app: Context) = Room.databaseBuilder(
        app,
        AppDatabase::class.java,
        Constants.DATABASE_NAME
    ).addCallback(object : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            AppDatabase.mIsDatabaseCreated.postValue(true)
            Log.d("AppDatabase", "buildDatabase onCreate" + db.path)
        }

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            Log.d("AppDatabase", "buildDatabase onOpen" + db.path)
        }

        override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
            super.onDestructiveMigration(db)
            Log.d("AppDatabase", "buildDatabase onDestructiveMigration" + db.path)
        }
    }).addMigrations(AppDatabase.MIGRATION_1_2).build()

    @Singleton
    @Provides
    fun provideSharedPreferences(
        @ApplicationContext app: Context
    ) = app.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)!!

    @Singleton
    @Provides
    fun provideLoginInfoDao(db: AppDatabase) = db.loginInfoDao()

    @Singleton
    @Provides
    fun provideXmlTemplateDao(db: AppDatabase) = db.xmlTemplateDao()

    @Singleton
    @Provides
    fun provideRestConnector() = RestConnector()

    @Singleton
    @Provides
    fun provideUserSettings() = UserSettings()

    @Singleton
    @Provides
    fun provideAppSettings() = AppSettings()

    @Singleton
    @Provides
    fun provideAppSize() = AppSize()

}