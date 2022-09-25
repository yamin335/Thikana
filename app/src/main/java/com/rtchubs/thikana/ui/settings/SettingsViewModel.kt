package com.rtchubs.thikana.ui.settings

import android.app.Application
import com.rtchubs.thikana.ui.common.BaseViewModel
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val application: Application
) : BaseViewModel(application) {

}