package com.taipt.tiktokdownloaderapp.data.local.save.video

import android.content.ContentResolver
import com.taipt.tiktokdownloaderapp.data.model.VideoInSavingIntoFile
import java.io.IOException
import javax.inject.Inject

interface SaveVideoFile {

    @Throws(IOException::class)
    operator fun invoke(directory: String, fileName: String, videoInProcess: VideoInSavingIntoFile): String?
}