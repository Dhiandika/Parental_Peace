package com.dicoding.parentalpeaceapp.ui.main.bottomnavigation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.parentalpeaceapp.data.Repository
import com.dicoding.parentalpeaceapp.data.UserModel
import com.dicoding.parentalpeaceapp.response.DataItem

class HomeViewModel(private val repository: Repository) : ViewModel() {
    fun listArticle(): MutableLiveData<List<DataItem>?> {
        repository.listArticle()
        return repository.listArticle
    }
}
