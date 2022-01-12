package com.avenue.baseframework.core.ui.activities.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avenue.baseframework.core.db.entity.LoginInfoEntity
import com.avenue.baseframework.core.models.UserSettings
import com.avenue.baseframework.core.repository.DataRepository
import com.avenue.baseframework.core.utils.NetUtils
import com.avenue.baseframework.restclient.RestConnector
import com.avenue.baseframework.restclient.utils.RestOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.ArrayList
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    private val restConnector: RestConnector,
    private val userSettings: UserSettings
) : ViewModel() {

    val TAG = "LoginViewModel"

    val loginFormState: MutableLiveData<LoginFormState> = MutableLiveData<LoginFormState>()
    var mUserLoginInfo: LiveData<List<LoginInfoEntity>>? = null

    var loginInfoList: List<LoginInfoEntity>? = null

    init {
        mUserLoginInfo = dataRepository.loadAllLoginInfoData()
    }

    fun login(username: String, password: String?) {
        var isOfflineLogin = false
        loginInfoList?.let {
            if (it.isNotEmpty()) {
                val loginInfoEntity = findLoginInfo(username)
                loginInfoEntity?.let { lie ->
                    if (lie.USER_PASSWORD == password) {
                        loginFormState.value = LoginFormState(true)
                        userSettings.setUserInfo(username, password)
                        isOfflineLogin = true
                    }
                }
            }
        }
        if (!isOfflineLogin) {
            if (NetUtils.isNetworkAvailable()) {
                val restOptions: RestOptions = restConnector.restOptions!!
                restOptions.userName = username
                restOptions.password = password!!
                restConnector.login(restOptions, {
                    loginFormState.value = LoginFormState(true)
                    userSettings.setUserInfo(username, password)
                    val loginInfoEntity = findLoginInfo(username)
                    if (loginInfoEntity == null) {
                        val list: MutableList<LoginInfoEntity> = ArrayList()
                        list.add(LoginInfoEntity(username, password))
                        viewModelScope.launch {
                            dataRepository.insertAllLoginInfo(list)
                        }
                    } else {
                        viewModelScope.launch {
                            dataRepository.updateUserPassword(username, password)
                        }
                    }
                }) { loginFormState.setValue(LoginFormState(false)) }
            } else {
                loginFormState.setValue(LoginFormState(1, 1))
            }
        }
    }

    private fun findLoginInfo(username: String): LoginInfoEntity? {
        var manPowerEntity: LoginInfoEntity? = null
        loginInfoList?.let {
            for (entity in it) {
                if (entity.USER_NAME == username) {
                    manPowerEntity = entity
                    break
                }
            }
        }
        return manPowerEntity
    }
}