package com.taipt.tiktokdownloaderapp.settings

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Build.VERSION_CODES.N
import android.os.LocaleList
import androidx.annotation.RequiresApi
import androidx.preference.PreferenceManager
import java.util.*


class LocaleHelper(
    val context: Context
) {

    companion object {
        const val LANGUAGE_ENGLISH = "en"
        const val LANGUAGE_VIETNAMESE = "vi"
        const val LANGUAGE_KOREAN = "ko"
        private const val LANGUAGE_KEY = "language_key"

        fun getLocale(resources: Resources): Locale {
            val config = resources.configuration
            return if (Build.VERSION.SDK_INT >= N) {
                config.locales[0]
            } else {
                config.locale
            }
        }
    }

    private var sharedPreferences: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)

    fun setLocale(context: Context): Context {
        return updateResources(context, getLanguage())
    }

    fun setNewLocale(context: Context, language: String): Context {
        persistLanguage(language)
        return updateResources(context, language)
    }

    private fun updateResources(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val res: Resources = context.resources
        val config = Configuration(res.configuration)
        return if (Build.VERSION.SDK_INT >= N) {
            setLocaleForApi24(config, locale)
            context.createConfigurationContext(config)
        } else {
            config.locale = locale
            res.updateConfiguration(config, res.displayMetrics)
            context
        }
    }

    fun getLanguage(): String {
        return sharedPreferences.getString(LANGUAGE_KEY, LANGUAGE_ENGLISH) ?: LANGUAGE_ENGLISH
    }

    @SuppressLint("ApplySharedPref")
    fun persistLanguage(language: String) {
        sharedPreferences.edit().putString(LANGUAGE_KEY, language).commit()
    }

    @RequiresApi(api = N)
    fun setLocaleForApi24(config: Configuration, target: Locale) {
        val set: MutableSet<Locale> = LinkedHashSet()
        // bring the target locale to the front of the list
        // bring the target locale to the front of the list
        set.add(target)

        val all: LocaleList = LocaleList.getDefault()
        for (i in 0 until all.size()) {
            // append other locales supported by the user
            set.add(all.get(i))
        }

        val locales: Array<Locale> = set.toTypedArray()
        config.setLocales(LocaleList(*locales))
    }

//    private var sharedPreferences: SharedPreferences =
//        context.getSharedPreferences("LANG", Context.MODE_PRIVATE)

//    fun setLocale(language: String): Context {
//        Logger.logMessage("setLocale -- $language")
//
//        // updating the language for devices above android nougat
//        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            updateResources(language)
//        } else {
//            updateResourcesLegacy(language)
//        }
//    }


//    @SuppressLint("ApplySharedPref")
//    fun setLanguage(language: String) {
//        val editor = sharedPreferences.edit()
//        editor.putString("lang", language)
//        editor.commit()
//    }
//
//    fun getLanguage(): String? {
//        return sharedPreferences.getString("lang", "en")
//    }

//    @TargetApi(Build.VERSION_CODES.N)
//    private fun updateResources(language: String): Context {
//        val locale = Locale(language)
//        Locale.setDefault(locale)
//        val configuration = context.resources.configuration
//        configuration.setLocale(locale)
//        configuration.setLayoutDirection(locale)
//        setLanguage(language)
//        return context.createConfigurationContext(configuration)
//    }
//
//    @SuppressWarnings("deprecation")
//    private fun updateResourcesLegacy(language: String): Context {
//        val locale = Locale(language)
//        Locale.setDefault(locale)
//        val resources = context.resources
//        val configuration = resources.configuration
//        configuration.locale = locale
//        configuration.setLayoutDirection(locale)
//        resources.updateConfiguration(configuration, resources.displayMetrics)
//        setLanguage(language)
//
//        retuy
//    }


}