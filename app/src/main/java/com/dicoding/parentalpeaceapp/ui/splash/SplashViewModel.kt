package com.dicoding.parentalpeaceapp.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.parentalpeaceapp.data.Repository
import com.dicoding.parentalpeaceapp.data.UserModel
import com.dicoding.parentalpeaceapp.response.SignUpResponse
import com.dicoding.parentalpeaceapp.result.Result
import kotlinx.coroutines.launch

class SplashViewModel(private val repository: Repository) : ViewModel() {
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
}