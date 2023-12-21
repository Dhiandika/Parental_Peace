package com.dicoding.parentalpeaceapp.ui.forgot

import androidx.lifecycle.ViewModel
import com.dicoding.parentalpeaceapp.data.Repository

class ForgotViewModel(private val repository: Repository) : ViewModel()  {

    fun forgot(email: String) =
        repository.forgot(email)

}