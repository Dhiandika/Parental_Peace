package com.dicoding.parentalpeaceapp.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import com.dicoding.parentalpeaceapp.R
import com.dicoding.parentalpeaceapp.ui.ViewModelFactory
import com.dicoding.parentalpeaceapp.ui.main.MainActivity
import com.dicoding.parentalpeaceapp.ui.welcome.WelcomeActivity

class SplashActivity : AppCompatActivity() {

    private val viewModel by viewModels<SplashViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()

        viewModel.getSession().observe(this){
            Handler(Looper.getMainLooper()).postDelayed({
                //ohhh

            if (!it.isLogin){
                val intent = Intent(this, WelcomeActivity::class.java)
                startActivity(intent)
                finish()
            } else{
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            }, 1500L)
        }



    }
}