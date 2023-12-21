package com.dicoding.parentalpeaceapp.data

data class UserModel(
    val name: String,
    val email: String,
    val phone: String,
    val userId: String,
    val token: String,
    val isLogin: Boolean = false
)