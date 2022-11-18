package com.taipt.mp3tik.data.network.parsing.converter

import com.taipt.mp3tik.data.network.response.VideoResponse
import okhttp3.ResponseBody

class VideoResponseConverter : ParsingExceptionThrowingConverter<VideoResponse>() {

    override fun convertSafely(responseBody: ResponseBody): VideoResponse? =
        VideoResponse(responseBody.contentType(), responseBody.byteStream())
}