package com.rtchubs.thikana.ui.topup

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.rtchubs.thikana.ui.common.BaseViewModel
import javax.inject.Inject

class TopUpMobileViewModel @Inject constructor(private val application: Application) : BaseViewModel(application) {

    val mobileNo: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
}