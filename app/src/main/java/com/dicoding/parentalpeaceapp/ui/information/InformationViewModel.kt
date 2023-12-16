package com.dicoding.parentalpeaceapp.ui.information

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.parentalpeaceapp.data.Repository
import com.dicoding.parentalpeaceapp.response.DataItem

class InformationViewModel(private val repository: Repository) : ViewModel() {
    fun listArticle(): MutableLiveData<List<DataItem>?> {
        repository.listArticle()
        return repository.listArticle
    }
}
