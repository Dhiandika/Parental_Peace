package com.dicoding.parentalpeaceapp.ui.fragment.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.dicoding.parentalpeaceapp.data.Repository
import com.dicoding.parentalpeaceapp.di.Injection
import com.dicoding.parentalpeaceapp.response.DataItem
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: Repository) : ViewModel() {
    fun listArticle(): MutableLiveData<List<DataItem>?> {
        repository.listArticle()
        return repository.listArticle
    }
}
