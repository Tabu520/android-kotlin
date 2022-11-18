package com.taipt.tiktokdownloaderapp.data.network.parsing.converter

import com.taipt.extractaudio.network.exceptions.CaptchaRequiredException
import com.taipt.extractaudio.network.exceptions.ParsingException
import okhttp3.ResponseBody
import retrofit2.Converter

abstract class ParsingExceptionThrowingConverter<T>: Converter<ResponseBody, T> {

    @Throws(ParsingException::class, CaptchaRequiredException::class)
    final override fun convert(value: ResponseBody): T? =
        try {
            convertSafely(value)
        } catch (captchaRequiredException: CaptchaRequiredException) {
            throw  captchaRequiredException
        } catch (throwable: Throwable) {
            throw ParsingException(cause = throwable)
        }

    abstract fun convertSafely(responseBody: ResponseBody): T?
}