package com.dicoding.parentalpeaceapp.ui.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.parentalpeaceapp.data.Repository
import com.dicoding.parentalpeaceapp.data.UserModel
import kotlinx.coroutines.launch

class SignInViewModel(private val repository: Repository) : ViewModel()  {

    fun signin(email: String, password: String) =
        repository.signIn(email, password)

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}