package com.rtchubs.thikana.ui.pin_number

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.rtchubs.thikana.api.*
import com.rtchubs.thikana.models.registration.DefaultResponse
import com.rtchubs.thikana.repos.LoginRepository
import com.rtchubs.thikana.ui.common.BaseViewModel
import com.rtchubs.thikana.util.AppConstants
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

class PinNumberViewModel @Inject constructor(
    private val application: Application,
    private val repository: LoginRepository
) : BaseViewModel(application) {
    val pin: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val rePin: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val defaultResponse: MutableLiveData<DefaultResponse> = MutableLiveData()

    fun connectToken(
        userName: String,
        password: String,
        grantType: String,
        scope: String,
        deviceID: String,
        deviceName: String,
        deviceModel: String,
        clientID: String,
        clientSecret: String,
        otp: String
    ) {
        if (checkNetworkStatus()) {
            val handler = CoroutineExceptionHandler { _, exception ->
                exception.printStackTrace()
                apiCallStatus.postValue(ApiCallStatus.ERROR)
                toastError.postValue(AppConstants.serverConnectionErrorMessage)
            }

            apiCallStatus.postValue(ApiCallStatus.LOADING)
            viewModelScope.launch(handler) {
                when (val apiResponse =
                    ApiResponse.create(
                        repository.loginRepo(
                            userName,
                            password,
                            grantType,
                            scope,
                            deviceID,
                            deviceName,
                            deviceModel,
                            clientID,
                            clientSecret,
                            otp
                        )
                    )) {
                    is ApiSuccessResponse -> {
                        defaultResponse.postValue(
                            DefaultResponse(
                                apiResponse.body.toString(),
                                "",
                                "",
                                true
                            )
                        )
                        apiCallStatus.postValue(ApiCallStatus.SUCCESS)
                    }
                    is ApiEmptyResponse -> {
                        apiCallStatus.postValue(ApiCallStatus.EMPTY)
                    }
                    is ApiErrorResponse -> {
                        defaultResponse.postValue(
                            Gson().fromJson(
                                apiResponse.errorMessage,
                                DefaultResponse::class.java
                            )
                        )
                        apiCallStatus.postValue(ApiCallStatus.ERROR)
                    }
                }
            }
        }
    }
}