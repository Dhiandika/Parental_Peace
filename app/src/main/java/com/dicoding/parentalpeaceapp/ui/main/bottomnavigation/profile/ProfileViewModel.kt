package com.dicoding.parentalpeaceapp.ui.main.bottomnavigation.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.parentalpeaceapp.data.UserModel
import com.dicoding.parentalpeaceapp.response.LoginResult

class ProfileViewModel : ViewModel(){
    private val _loginResult = MutableLiveData<LoginResult>()
    // LiveData untuk menyimpan data profil
    private val _profileData = MutableLiveData<UserModel>()
    val profileData: LiveData<UserModel> get() = _profileData
    // Metode untuk mengatur data profil
    fun setProfileData(data: UserModel) {
        _profileData.value = data
    }
}