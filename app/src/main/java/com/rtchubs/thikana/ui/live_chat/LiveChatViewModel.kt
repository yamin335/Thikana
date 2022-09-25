package com.rtchubs.thikana.ui.live_chat

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.rtchubs.thikana.local_db.dao.CartDao
import com.rtchubs.thikana.models.LiveChatMessage
import com.rtchubs.thikana.ui.common.BaseViewModel
import javax.inject.Inject

class LiveChatViewModel @Inject constructor(
    private val application: Application,
    private val cartDao: CartDao
    ) : BaseViewModel(application) {

    val newMessage: MutableLiveData<String> = MutableLiveData()

    val chatMessages: MutableLiveData<MutableList<LiveChatMessage>> by lazy {
        MutableLiveData<MutableList<LiveChatMessage>>()
    }

}