package com.rtchubs.thikana.ui.transactions

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rtchubs.thikana.api.*
import com.rtchubs.thikana.models.order.SalesData
import com.rtchubs.thikana.repos.OrderRepository
import com.rtchubs.thikana.ui.common.BaseViewModel
import com.rtchubs.thikana.util.AppConstants
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

class TransactionsViewModel @Inject constructor(
    private val application: Application,
    private val orderRepository: OrderRepository
) : BaseViewModel(application) {

    val orderItems: MutableLiveData<List<SalesData>> by lazy {
        MutableLiveData<List<SalesData>>()
    }

    fun getOrderList(page: Int?, token: String?) {
        if (checkNetworkStatus()) {
            val handler = CoroutineExceptionHandler { _, exception ->
                exception.printStackTrace()
                apiCallStatus.postValue(ApiCallStatus.ERROR)
                toastError.postValue(AppConstants.serverConnectionErrorMessage)
            }

            apiCallStatus.postValue(ApiCallStatus.LOADING)
            viewModelScope.launch(handler) {
                when (val apiResponse = ApiResponse.create(orderRepository.getOrderList(page, token))) {
                    is ApiSuccessResponse -> {
                        apiCallStatus.postValue(ApiCallStatus.SUCCESS)
                        orderItems.postValue(apiResponse.body.data?.sales?.data)
                    }
                    is ApiEmptyResponse -> {
                        apiCallStatus.postValue(ApiCallStatus.EMPTY)
                    }
                    is ApiErrorResponse -> {
                        apiCallStatus.postValue(ApiCallStatus.ERROR)
                    }
                }
            }
        }
    }

}