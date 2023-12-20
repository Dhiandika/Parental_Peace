package com.dicoding.parentalpeaceapp.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.parentalpeaceapp.data.Repository
import com.dicoding.parentalpeaceapp.response.SignUpResponse
import com.dicoding.parentalpeaceapp.result.Result
import kotlinx.coroutines.launch

class SignUpViewModel(private val repository: Repository) : ViewModel() {
    fun register(
        username: String,
        email: String,
        phone: String,
        password: String
    ): LiveData<Result<SignUpResponse>> = repository.register(username, email, phone, password)

    fun getSession() {
        viewModelScope.launch {
            repository.getSession()
        }
    }
}