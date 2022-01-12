package com.avenue.baseframework.core.ui.activities.network

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.AuthFailureError
import com.android.volley.VolleyError
import com.avenue.baseframework.core.models.UserSettings
import com.avenue.baseframework.restclient.RestConnector
import com.avenue.baseframework.restclient.utils.RestOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NetworkViewModel @Inject constructor(
    private val restConnector: RestConnector,
    private val userSettings: UserSettings
): ViewModel() {

    val TAG = "NetworkViewModel"
    var mHost: MutableLiveData<String> = MutableLiveData()
    var mPort: MutableLiveData<String> = MutableLiveData()
    var mWebPort: MutableLiveData<String> = MutableLiveData()
    var networkFormState: MutableLiveData<NetworkFormState> = MutableLiveData<NetworkFormState>()

    init {
        mHost.value = userSettings.getServerHost()
        mPort.value = userSettings.getServerPort()
        mWebPort.value = userSettings.getServerWebPort()
    }

    fun setNetworkInfo(host: String, port: String, webPort: String) {
        userSettings.setNetworkSettings(host, port, webPort)
    }

    fun validateServer(host: String, port: String, webPort: String?) {
        val restOptions = RestOptions(host, port.toInt())
        restConnector.validateServer(restOptions, { jsonObject ->
            Log.d(TAG, "validateServerComplete: $jsonObject")
        }) { error: VolleyError? ->
            if (error is AuthFailureError) {
                restConnector.restOptions = restOptions
                networkFormState.value = NetworkFormState(true)
                setNetworkInfo(host, port, webPort!!)
            } else {
                networkFormState.value = NetworkFormState(false)
            }
        }
    }

}