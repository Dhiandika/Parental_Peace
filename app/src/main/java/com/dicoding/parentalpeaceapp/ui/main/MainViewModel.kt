package com.dicoding.parentalpeaceapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.parentalpeaceapp.data.Repository
import com.dicoding.parentalpeaceapp.data.UserModel
import java.io.File

class MainViewModel(private val repository: Repository) : ViewModel() {
    fun uploadAudio(file: File) = repository.uploadAudio(file)

}