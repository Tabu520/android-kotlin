package com.taipt.tiktokdownloaderapp.data.network.parsing

import com.taipt.tiktokdownloaderapp.data.network.parsing.converter.ActualVideoPageUrlConverter
import com.taipt.tiktokdownloaderapp.data.network.parsing.converter.ThrowIfIsCaptchaResponse
import com.taipt.tiktokdownloaderapp.data.network.parsing.converter.VideoFileUrlConverter
import com.taipt.tiktokdownloaderapp.data.network.parsing.converter.VideoResponseConverter
import com.taipt.extractaudio.network.response.ActualVideoPageUrl
import com.taipt.extractaudio.network.response.VideoFileUrl
import com.taipt.extractaudio.network.response.VideoResponse
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class TikTokWebPageConverterFactory(
    private val throwIfIsCaptchaResponse: ThrowIfIsCaptchaResponse
): Converter.Factory() {

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? =
        when (type) {
            ActualVideoPageUrl::class.java -> ActualVideoPageUrlConverter(throwIfIsCaptchaResponse)
            VideoFileUrl::class.java -> VideoFileUrlConverter(throwIfIsCaptchaResponse)
            VideoResponse::class.java -> VideoResponseConverter()
            else -> super.responseBodyConverter(type, annotations, retrofit)
        }
}