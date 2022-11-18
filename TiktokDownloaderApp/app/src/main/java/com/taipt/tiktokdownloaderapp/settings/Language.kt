package com.taipt.tiktokdownloaderapp.utils

import java.util.Locale

object LocaleCodes {
    const val ENGLISH = "en"
    const val VIETNAMESE = "vi"
}

enum class Language(val locale: Locale) {
    English(Locale(LocaleCodes.ENGLISH)),
    Vietnamese(Locale(LocaleCodes.VIETNAMESE));

    companion object {
        val DEFAULT = English

        fun fromLocale(locale: Locale): Language =
            values().firstOrNull {
                it.locale.language == locale.language
            } ?: DEFAULT
    }
}