package com.dicoding.parentalpeaceapp.ui.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.parentalpeaceapp.ui.ui.notifications.NotificationsViewModel

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotificationsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NotificationsViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}