package com.taipt.tiktokdownloaderapp.data.network.parsing.converter

import com.taipt.tiktokdownloaderapp.data.network.response.VideoResponse
import okhttp3.ResponseBody

class VideoResponseConverter : ParsingExceptionThrowingConverter<VideoResponse>() {

    override fun convertSafely(responseBody: ResponseBody): VideoResponse? =
        VideoResponse(responseBody.contentType(), responseBody.byteStream())
}