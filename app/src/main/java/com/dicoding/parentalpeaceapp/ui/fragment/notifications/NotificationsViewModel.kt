package com.dicoding.parentalpeaceapp.ui.fragment.notifications

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.dicoding.parentalpeaceapp.R
import kotlinx.coroutines.Dispatchers

class NotificationsViewModel(private val context: Context) : ViewModel() {

    val text = liveData(Dispatchers.IO) {
        emit(context.getString(R.string.no_notification))
    }
}
