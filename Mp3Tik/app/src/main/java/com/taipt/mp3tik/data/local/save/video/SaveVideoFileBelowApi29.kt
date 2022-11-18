package com.taipt.mp3tik.data.local.save.video

import android.content.ContentResolver
import android.os.Environment
import android.provider.MediaStore
import com.taipt.mp3tik.data.model.VideoInSavingIntoFile
import java.io.File

@Suppress("DEPRECATION")
class SaveVideoFileBelowApi29(private val resolver: ContentResolver) : SaveVideoFile {

    override fun invoke(directory: String, fileName: String, videoInProcess: VideoInSavingIntoFile): String? {
        val finalFileName = "TiktokVideo_$fileName"
        val externalDirectory =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
        externalDirectory.mkdir()
        val videoDirectory = File(externalDirectory, directory)
        videoDirectory.mkdir()
        val videoFile = File(videoDirectory, finalFileName)
        videoFile.createNewFile()
        videoInProcess.byteStream.safeCopyInto(videoFile.outputStream())

        val values = buildDefaultVideoContentValues(
            fileName = finalFileName,
            contentType = videoInProcess.contentType?.toString()
        ) {
            put(MediaStore.Video.Media.DATA, videoFile.path)
        }

        return resolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values)?.toString()
    }
}