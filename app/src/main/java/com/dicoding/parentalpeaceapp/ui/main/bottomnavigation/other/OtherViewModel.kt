package com.dicoding.parentalpeaceapp.ui.main.bottomnavigation.other

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.parentalpeaceapp.data.Repository
import kotlinx.coroutines.launch

class OtherViewModel(private val repository: Repository) : ViewModel() {

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}