package com.dicoding.parentalpeaceapp.ui

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.dicoding.parentalpeaceapp.R
import com.dicoding.parentalpeaceapp.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_home) as NavHostFragment
        navController =navHostFragment.navController
        val navView: BottomNavigationView = binding.navView

        setupWithNavController(navView, navController)

        val showFragment = intent.getStringExtra("SHOW_FRAGMENT")
        if (showFragment != null) {
            // Tampilkan fragment sesuai dengan informasi yang diterima
            when (showFragment) {
                "HOME_FRAGMENT" -> navController.navigate(R.id.navigation_home)
                "NOTIFICATIONS_FRAGMENT" -> navController.navigate(R.id.navigation_notifications)
                "PROFILE_FRAGMENT" -> navController.navigate(R.id.navigation_profile)
                "OTHER_FRAGMENT" -> navController.navigate(R.id.navigation_other)
                // Tambahkan case untuk fragment lainnya sesuai kebutuhan
            }
        }
    }

}