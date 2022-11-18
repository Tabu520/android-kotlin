package com.taipt.tiktokdownloaderapp.di

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import com.taipt.tiktokdownloaderapp.settings.Language
import dagger.MapKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import java.util.Locale


@MustBeDocumented
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class LanguageKey(val value: Language)

@Module
@InstallIn(SingletonComponent::class)
class LocalizationModule {

    @Provides
    @IntoMap
    @LanguageKey(Language.English)
    fun provideEnglishResources(context: Context): Resources =
        getLocalizedResources(context, Language.English.locale)

    @Provides
    @IntoMap
    @LanguageKey(Language.Vietnamese)
    fun provideVietnameseResources(context: Context): Resources =
        getLocalizedResources(context, Language.Vietnamese.locale)

    private fun getLocalizedResources(context: Context, locale: Locale): Resources {
        val conf = Configuration(context.resources.configuration)
        conf.setLocale(locale)
        val localizedResources = context.createConfigurationContext(conf)
        return localizedResources.resources
    }
}