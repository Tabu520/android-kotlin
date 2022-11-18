package com.taipt.mp3tik.data.network.response

import okhttp3.MediaType
import java.io.InputStream

data class VideoResponse(val mediaType: MediaType?, val videoInputStream: InputStream)
