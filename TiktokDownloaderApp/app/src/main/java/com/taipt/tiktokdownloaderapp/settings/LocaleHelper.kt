package com.taipt.tiktokdownloaderapp.utils

import android.annotation.TargetApi
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.preference.PreferenceManager
import java.util.Locale

class LocaleHelper(
    val context: Context
) {

    private val SELECTED_LANGUAGE: String = "Locale.Helper.Selected.Language"
    private var sharedPreferences: SharedPreferences =
        context.getSharedPreferences(SELECTED_LANGUAGE, Context.MODE_PRIVATE)

    fun setLocale(language: String): Context {
        persist(language);

        // updating the language for devices above android nougat
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResources(context, language);
        }
        // for devices having lower version of android os
        return updateResourcesLegacy(context, language);
    }

    private fun persist(language: String) {
        val editor = sharedPreferences.edit()
        editor.putString(SELECTED_LANGUAGE, language)
        editor.apply()
    }

    fun getLanguage(): String {
        return sharedPreferences.getString(SELECTED_LANGUAGE, "en") ?: "en"
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun updateResources(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val configuration = context.resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)

        return context.createConfigurationContext(configuration)
    }

    @SuppressWarnings("deprecation")
    private fun updateResourcesLegacy(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val resources = context.resources
        val configuration = resources.configuration
        configuration.locale = locale
        configuration.setLayoutDirection(locale)

        resources.updateConfiguration(configuration, resources.displayMetrics)

        return context
    }
}