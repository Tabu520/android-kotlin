package com.taipt.tiktokdownloaderapp.data.network.parsing.converter

import com.taipt.tiktokdownloaderapp.data.network.exceptions.CaptchaRequiredException
import com.taipt.tiktokdownloaderapp.data.network.response.VideoFileUrl
import com.taipt.tiktokdownloaderapp.utils.Logger
import okhttp3.ResponseBody

class VideoFileUrlConverter(
    private val throwIfIsCaptchaResponse: ThrowIfIsCaptchaResponse
): ParsingExceptionThrowingConverter<VideoFileUrl>() {

    @Throws(IllegalArgumentException::class, IndexOutOfBoundsException::class, CaptchaRequiredException::class)
    override fun convertSafely(responseBody: ResponseBody): VideoFileUrl? {
        val html = responseBody.string().also(throwIfIsCaptchaResponse::invoke)
        val url = tryToParseDownloadLink(html).also { Logger.logMessage("parsed download link = $it") }
            ?: tryToParseVideoSrc(html).also { Logger.logMessage("parsed video src = $it") }
            ?: throw IllegalArgumentException("Couldn't parse url from HTML: $html")

        return VideoFileUrl(url)
    }

    companion object {

        private fun tryToParseDownloadLink(html: String): String? =
            if (html.contains("\"playAddr\"")) {
                html.split("\"playAddr\"")[1]
                    .dropWhile { it != '\"' }.drop(1)
                    .takeWhile { it != '\"' }
                    .urlCharacterReplacements()
            } else {
                null
            }

        private fun tryToParseVideoSrc(html: String): String? =
            if (html.contains("<video")) {
                html.split("<video")[1]
                    .split("</video>")[0]
                    .split("src")[1]
                    .dropWhile { it != '=' }
                    .dropWhile { it != '\"' }.drop(1)
                    .takeWhile { it != '\"' }
                    .urlCharacterReplacements()
            } else {
                null
            }

        private val replacements = mutableMapOf(
            "\\u002F" to "/",
            "\\u0026" to "&"
        )

        private fun String.urlCharacterReplacements(): String =
            replacements.entries.fold(this) { result, toReplaceEntry ->
                result.replace(toReplaceEntry.key, toReplaceEntry.value)
            }
    }
}