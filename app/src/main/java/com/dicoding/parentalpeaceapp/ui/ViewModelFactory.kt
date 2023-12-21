package com.dicoding.parentalpeaceapp.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.parentalpeaceapp.data.Repository
import com.dicoding.parentalpeaceapp.di.Injection
import com.dicoding.parentalpeaceapp.ui.consultation.ConsultationViewModel
import com.dicoding.parentalpeaceapp.ui.forgot.ForgotViewModel
import com.dicoding.parentalpeaceapp.ui.main.bottomnavigation.home.HomeViewModel
import com.dicoding.parentalpeaceapp.ui.information.InformationViewModel
import com.dicoding.parentalpeaceapp.ui.main.MainViewModel
import com.dicoding.parentalpeaceapp.ui.main.bottomnavigation.other.OtherViewModel
import com.dicoding.parentalpeaceapp.ui.signin.SignInViewModel
import com.dicoding.parentalpeaceapp.ui.signup.SignUpViewModel
import com.dicoding.parentalpeaceapp.ui.splash.SplashViewModel

class ViewModelFactory(private val repository: Repository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> {
                SplashViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SignUpViewModel::class.java) -> {
                SignUpViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SignInViewModel::class.java) -> {
                SignInViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ForgotViewModel::class.java) -> {
                ForgotViewModel(repository) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }
            modelClass.isAssignableFrom(InformationViewModel::class.java) -> {
                InformationViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ConsultationViewModel::class.java) -> {
                ConsultationViewModel(repository) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }
            modelClass.isAssignableFrom(OtherViewModel::class.java) -> {
                OtherViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(Injection.provideRepository(context))
                }
            }
            return INSTANCE as ViewModelFactory
        }

    }
}