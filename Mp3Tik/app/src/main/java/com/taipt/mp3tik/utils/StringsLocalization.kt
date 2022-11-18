package com.taipt.mp3tik.utils

import android.content.res.Resources
import androidx.annotation.StringRes
import com.taipt.mp3tik.settings.AppSettings
import com.taipt.mp3tik.settings.Language
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StringsLocalization @Inject constructor(
    private val appSettings: AppSettings,
    private val resMap: Map<Language, @JvmSuppressWildcards Resources>
) {

    fun getString(@StringRes stringId: Int): String = resMap
        .getOrElse(appSettings.currentLanguage, this::getFallbackResources)
        .getString(stringId)

    private fun getFallbackResources(): Resources {
        val defaultLanguage =
            if (Language.DEFAULT in resMap) Language.DEFAULT
            else resMap.keys.firstOrNull()

        if (defaultLanguage != null) {
            Logger.logMessage("Current language resources not found! Fallback to $defaultLanguage")
            appSettings.currentLanguage = defaultLanguage

            return resMap[defaultLanguage]!!
        } else {
            throw ResourcesNotFoundException("String resources not found")
        }
    }
}

class ResourcesNotFoundException(message: String): RuntimeException(message)