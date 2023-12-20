package com.dicoding.parentalpeaceapp.di

import android.content.Context
import com.dicoding.parentalpeaceapp.data.Repository
import com.dicoding.parentalpeaceapp.data.UserPreference
import com.dicoding.parentalpeaceapp.data.dataStore
import com.dicoding.parentalpeaceapp.data.database.ParentalPeaceDatabase
import com.dicoding.parentalpeaceapp.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): Repository {
        val pref = UserPreference.getInstance(context.dataStore)
        val database = ParentalPeaceDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return Repository(database, pref, apiService)
    }
}