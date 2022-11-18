package com.taipt.tiktokdownloaderapp.data.repository

import com.taipt.extractaudio.network.exceptions.CaptchaRequiredException
import com.taipt.extractaudio.network.exceptions.NetworkException
import com.taipt.extractaudio.network.exceptions.ParsingException
import com.taipt.tiktokdownloaderapp.data.local.exceptions.StorageException
import com.taipt.tiktokdownloaderapp.data.model.VideoInPending
import com.taipt.tiktokdownloaderapp.data.model.VideoInSavingIntoFile
import com.taipt.tiktokdownloaderapp.data.network.TikTokApi
import com.taipt.tiktokdownloaderapp.utils.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TiktokDownloadRepository @Inject constructor(
    private val api: TikTokApi
) {

    @Throws(StorageException::class)
    fun saveVideo(videoInProcess: VideoInSavingIntoFile): VideoDownloaded {
        val fileName = videoInProcess.fileName()
        val uri = try {
            saveVideoFile("TikTok_Downloader", fileName, videoInProcess)
        } catch (throwable: Throwable) {
            throw StorageException(cause = throwable)
        }
        uri ?: throw StorageException("Uri couldn't be created")

        val result = VideoDownloaded(id = videoInProcess.id, url = videoInProcess.url, uri = uri)
        saveVideoDownloaded(result)

        return result
    }

    @Throws(ParsingException::class, NetworkException::class, CaptchaRequiredException::class)
    suspend fun getVideo(videoInPending: VideoInPending): VideoInSavingIntoFile = withContext(
        Dispatchers.IO) {
//        cookieStore.clear()
        wrapIntoProperException {
            delay(2000)
//            delay(delayBeforeRequest) // added just so captcha trigger may not happen
            val actualUrl = api.getContentActualUrlAndCookie(videoInPending.url)
            Logger.logMessage("actualUrl found = ${actualUrl.url}")
//            delay(delayBeforeRequest) // added just so captcha trigger may not happen
            val videoUrl = api.getVideoUrl(actualUrl.url)
            Logger.logMessage("videoFileUrl found = ${videoUrl.videoFileUrl}")
//            delay(delayBeforeRequest) // added just so captcha trigger may not happen
            val response = api.getVideo(videoUrl.videoFileUrl)

            VideoInSavingIntoFile(
                id = videoInPending.id,
                url = videoInPending.url,
                contentType = response.mediaType?.let { VideoInSavingIntoFile.ContentType(it.type, it.subtype) },
                byteStream = response.videoInputStream
            )
        }
    }

    @Throws(ParsingException::class, NetworkException::class)
    private suspend fun <T> wrapIntoProperException(request: suspend () -> T): T =
        try {
            request()
        } catch (parsingException: ParsingException) {
            throw parsingException
        } catch (captchaRequiredException: CaptchaRequiredException) {
            throw captchaRequiredException
        } catch (throwable: Throwable) {
            throw NetworkException(cause = throwable)
        }
}