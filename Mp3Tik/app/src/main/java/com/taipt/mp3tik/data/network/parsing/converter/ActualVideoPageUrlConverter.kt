package com.taipt.mp3tik.data.network.parsing.converter

import com.taipt.mp3tik.data.network.exceptions.CaptchaRequiredException
import com.taipt.mp3tik.data.network.response.ActualVideoPageUrl
import okhttp3.ResponseBody

class ActualVideoPageUrlConverter(
    private val throwIfIsCaptchaResponse: ThrowIfIsCaptchaResponse
): ParsingExceptionThrowingConverter<ActualVideoPageUrl>() {

    @Throws(IndexOutOfBoundsException::class, CaptchaRequiredException::class)
    override fun convertSafely(responseBody: ResponseBody): ActualVideoPageUrl? =
        responseBody.string()
            .also { throwIfIsCaptchaResponse::invoke }
            .split("rel=\"canonical\" href=\"")[1]
            .split("\"")[0]
            .let(::ActualVideoPageUrl)
}