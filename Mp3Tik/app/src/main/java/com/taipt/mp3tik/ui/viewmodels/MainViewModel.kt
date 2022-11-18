package com.taipt.mp3tik.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taipt.mp3tik.data.network.exceptions.CaptchaRequiredException
import com.taipt.mp3tik.data.network.exceptions.NetworkException
import com.taipt.extractaudio.network.exceptions.ParsingException
import com.taipt.mp3tik.data.model.VideoDownloaded
import com.taipt.mp3tik.data.model.VideoInPending
import com.taipt.mp3tik.data.model.VideoInSavingIntoFile
import com.taipt.mp3tik.data.repository.TiktokDownloadRepository
import com.taipt.mp3tik.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val tiktokDownloadRepository: TiktokDownloadRepository
): ViewModel() {

    val videoDownloadedMutableLiveData: MutableLiveData<Resource<VideoDownloaded>> = MutableLiveData()
    val videoSaveToFileMutableLiveData: MutableLiveData<Resource<VideoInSavingIntoFile>> = MutableLiveData()

    fun downloadVideo(videoInPending: VideoInPending) = viewModelScope.launch {
        safeDownloadVideoCall(videoInPending)
    }

    private suspend fun safeDownloadVideoCall(videoInPending: VideoInPending) {
        videoSaveToFileMutableLiveData.postValue(Resource.Loading())
        try {
            val videoInSavingIntoFile: VideoInSavingIntoFile = tiktokDownloadRepository.getVideo(videoInPending)
            val videoDownloaded: VideoDownloaded = tiktokDownloadRepository.saveVideo(videoInSavingIntoFile)
            videoDownloadedMutableLiveData.postValue(Resource.Success(videoDownloaded))
            videoSaveToFileMutableLiveData.postValue(Resource.Success(videoInSavingIntoFile))
        } catch (pe: ParsingException) {
            videoSaveToFileMutableLiveData.postValue(Resource.Error(null, pe.message ?: "Parsing Exception"))
        } catch (ne: NetworkException) {
            videoSaveToFileMutableLiveData.postValue(Resource.Error(null, ne.message ?: "Network Exception"))
        } catch (cre: CaptchaRequiredException) {
            videoSaveToFileMutableLiveData.postValue(Resource.Error(null, cre.message ?: "Captcha Required Exception"))
        }
    }

//    private fun hasInternetConnection(): Boolean {
//        Log.d("TaiPT", "NewsViewModel::hasInternetConnection()")
//        val connectivityManager =
//            getApplication<App>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        val activeNetwork = connectivityManager.activeNetwork ?: return false
//        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
//        return when {
//            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
//            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
//            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
//            else -> false
//        }
//    }
}