package com.taipt.mp3tik.data.local.save.video

import com.taipt.mp3tik.data.model.VideoInSavingIntoFile
import java.io.IOException

interface SaveVideoFile {

    @Throws(IOException::class)
    operator fun invoke(directory: String, fileName: String, videoInProcess: VideoInSavingIntoFile): String?
}