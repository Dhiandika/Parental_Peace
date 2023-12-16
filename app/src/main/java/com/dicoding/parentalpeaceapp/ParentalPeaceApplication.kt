package com.dicoding.parentalpeaceapp

import android.app.Application
import com.dicoding.parentalpeaceapp.data.Repository
import com.dicoding.parentalpeaceapp.data.database.ParentalPeaceDatabase
import com.dicoding.parentalpeaceapp.retrofit.ApiConfig

class ParentalPeaceApplication : Application() {

    lateinit var repository: Repository

    override fun onCreate() {
        super.onCreate()

        // Inisialisasi database
        val database = ParentalPeaceDatabase.getDatabase(this)

        // Inisialisasi ApiService menggunakan ApiConfig
        val apiService = ApiConfig.getApiService()

        // Inisialisasi Repository
        repository = Repository(database, apiService)
    }
}