package com.rtchubs.thikana.ui.profiles

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.rtchubs.thikana.prefs.PreferencesHelper
import com.rtchubs.thikana.ui.common.BaseViewModel
import javax.inject.Inject

class ProfilesViewModel  @Inject constructor(
    private val preferencesHelper: PreferencesHelper,
    private val application: Application) : BaseViewModel(application) {

    val name: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val mobile: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val email: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val address: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
}