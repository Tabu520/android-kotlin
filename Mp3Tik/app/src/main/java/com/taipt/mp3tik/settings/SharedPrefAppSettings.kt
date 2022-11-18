package com.taipt.mp3tik.settings

import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Build

interface AppSettings {
    var currentLanguage: Language
}

class SharedPrefAppSettings(
    private val sharedPref: SharedPreferences,
    private val androidConfiguration: Configuration
): AppSettings {

    private var currentLanguageCache: Language? = null

    override var currentLanguage: Language
        get() {
            val cachedValue = currentLanguageCache
            return if (cachedValue == null) {
                val storedValue = sharedPref.getString(APP_LANGUAGE_KEY, "")
                val storedLanguage = try {
                    if (storedValue != null) {
                        Language.valueOf(storedValue)
                    } else null
                } catch (ex: Exception) {
                    null
                }

                val language = storedLanguage ?: getDefaultLanguage()
                currentLanguage = language
                language
            } else cachedValue
        }
        set(value) {
            currentLanguageCache = value
            sharedPref.edit().putString(APP_LANGUAGE_KEY, value.toString()).apply()
        }

    private fun getDefaultLanguage(): Language {
        val locale= if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            androidConfiguration.locales[0]
        } else {
            androidConfiguration.locale
        }
        return Language.fromLocale(locale)
    }

    companion object {
        private const val APP_LANGUAGE_KEY = "app_language"
    }
}