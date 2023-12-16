package com.dicoding.parentalpeaceapp.ui

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.dicoding.parentalpeaceapp.R
import com.dicoding.parentalpeaceapp.databinding.ActivityHomeBinding
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var navController: NavController
    private lateinit var fab: FloatingActionButton

    private lateinit var bottomAppBar: BottomAppBar
    private var isRecording = false
    private var countdownTimer: CountDownTimer? = null
    private var recordingDialog: AlertDialog? = null

    private var shouldLaunchResultActivity = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fab = findViewById(R.id.fab)
        bottomAppBar = findViewById(R.id.bottomapp_bar)

        fab.setOnClickListener {
            if (!isRecording) {
                startRecording()
            } else {
                stopRecording()
            }
        }

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_home) as NavHostFragment
        navController =navHostFragment.navController
        val navView: BottomNavigationView = binding.navView

        setupWithNavController(navView, navController)

        val showFragment = intent.getStringExtra("SHOW_FRAGMENT")
        if (showFragment != null) {
            when (showFragment) {
                "HOME_FRAGMENT" -> navController.navigate(R.id.navigation_home)
                "NOTIFICATIONS_FRAGMENT" -> navController.navigate(R.id.navigation_notifications)
                "PROFILE_FRAGMENT" -> navController.navigate(R.id.navigation_profile)
                "OTHER_FRAGMENT" -> navController.navigate(R.id.navigation_other)
            }
        }
    }

    private fun startRecording() {
        // Start recording logic

        // Show recording dialog
        showRecordingDialog()

        // Simulate recording for 10 seconds using CountDownTimer
        countdownTimer = object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // Update UI during recording if needed
            }

            override fun onFinish() {
                // Finish recording after 10 seconds
                stopRecording()
            }
        }.start()

        // Update UI during recording if needed
        isRecording = true
        fab.setImageResource(R.drawable.baseline_mic_off_24) // Change icon to stop icon
    }

    private fun stopRecording() {
        // Stop recording logic
        countdownTimer?.cancel()

        // Update UI after recording is stopped
        isRecording = false
        fab.setImageResource(R.drawable.baseline_mic_none_72) // Change icon back to mic icon

        // Dismiss recording dialog
        recordingDialog?.dismiss()
    }

    private fun showRecordingDialog() {
        val dialogView = layoutInflater.inflate(R.layout.recording_dialog, null)
        val timeTextView: TextView = dialogView.findViewById(R.id.timeTextView)
        val cancelButton: Button = dialogView.findViewById(R.id.cancelButton)

        recordingDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        cancelButton.setOnClickListener {
            stopRecording()
            shouldLaunchResultActivity = false
        }

        recordingDialog?.show()

        // Initialize and start CountDownTimer for the dialog
        object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000
                val formattedTime = String.format("Recording %02d:%02d", seconds / 60, seconds % 60)
                timeTextView.text = formattedTime
            }

            override fun onFinish() {
                recordingDialog?.dismiss()

                if (shouldLaunchResultActivity) {
                    // Navigasi ke ResultActivity hanya jika shouldLaunchResultActivity true
                    val intent = Intent(this@MainActivity, ResultActivity::class.java)
                    startActivity(intent)
                }

                // Reset the flag for the next recording
                shouldLaunchResultActivity = true
            }
        }.start()
    }
}