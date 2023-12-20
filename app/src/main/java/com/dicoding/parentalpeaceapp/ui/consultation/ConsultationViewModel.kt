package com.dicoding.parentalpeaceapp.ui.consultation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.parentalpeaceapp.data.Repository
import com.dicoding.parentalpeaceapp.response.DataDoctorItem
import com.dicoding.parentalpeaceapp.response.DataItem

class ConsultationViewModel(private val repository: Repository) : ViewModel() {
    fun listDoctor(): MutableLiveData<List<DataDoctorItem>> {
        repository.listDoctor()
        return repository.listDoctor
    }
}