package com.taipt.tiktokdownloaderapp.di

import android.content.Context
import android.content.res.Configuration
import androidx.preference.PreferenceManager
import com.taipt.tiktokdownloaderapp.settings.AppSettings
import com.taipt.tiktokdownloaderapp.settings.SharedPrefAppSettings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppSettingsModule {

    @Provides
    @Singleton
    fun provideAppSettings(context: Context, configuration: Configuration): AppSettings =
        SharedPrefAppSettings(PreferenceManager.getDefaultSharedPreferences(context), configuration)
}