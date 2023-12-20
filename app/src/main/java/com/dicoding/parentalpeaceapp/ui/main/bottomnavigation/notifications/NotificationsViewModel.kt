package com.dicoding.parentalpeaceapp.ui.main.bottomnavigation.notifications

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.dicoding.parentalpeaceapp.R
import kotlinx.coroutines.Dispatchers

class NotificationsViewModel(private val context: Context) : ViewModel() {

    val text = liveData(Dispatchers.IO) {
        emit(context.getString(R.string.no_notification))
    }
}
