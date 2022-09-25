package com.rtchubs.thikana.ui.more

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.rtchubs.thikana.local_db.dao.CartDao
import com.rtchubs.thikana.ui.common.BaseViewModel
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class MoreViewModel @Inject constructor(
    private val application: Application,
    private val cartDao: CartDao
) : BaseViewModel(application) {
    val cartItemCount: LiveData<Int> = liveData {
        cartDao.getCartItemsCount().collect { count ->
            emit(count)
        }
    }
}