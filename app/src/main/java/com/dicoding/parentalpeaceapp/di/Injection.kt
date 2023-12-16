package com.dicoding.parentalpeaceapp.di

import android.content.Context
import com.dicoding.parentalpeaceapp.data.Repository
import com.dicoding.parentalpeaceapp.data.database.ParentalPeaceDatabase
import com.dicoding.parentalpeaceapp.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): Repository {
        val database = ParentalPeaceDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return Repository(database, apiService)
    }
}