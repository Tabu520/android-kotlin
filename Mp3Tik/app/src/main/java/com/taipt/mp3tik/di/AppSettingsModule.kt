package com.taipt.mp3tik.di

import android.content.Context
import android.content.res.Configuration
import androidx.preference.PreferenceManager
import com.taipt.mp3tik.settings.AppSettings
import com.taipt.mp3tik.settings.SharedPrefAppSettings
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