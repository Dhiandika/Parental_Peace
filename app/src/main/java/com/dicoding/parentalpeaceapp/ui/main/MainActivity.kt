package com.dicoding.parentalpeaceapp.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.dicoding.parentalpeaceapp.R
import com.dicoding.parentalpeaceapp.databinding.ActivityHomeBinding
import com.dicoding.parentalpeaceapp.result.Result
import com.dicoding.parentalpeaceapp.ui.translation.ResultActivity
import com.dicoding.parentalpeaceapp.ui.ViewModelFactory
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import okhttp3.internal.http2.Http2Reader
import java.io.File
import java.io.FileOutputStream

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityHomeBinding
    private lateinit var navController: NavController
    private lateinit var fab: FloatingActionButton

    private lateinit var bottomAppBar: BottomAppBar

    private var shouldLaunchResultActivity = true

    private val REQUEST_CODE = 123
    private var mediaRecorder: MediaRecorder? = null
    private var recordingDialog: androidx.appcompat.app.AlertDialog? = null
    private var currentRecordingFileName: String? = null

    private var isDialogShowing = false
    private var isRecording = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showLoading(false)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                REQUEST_CODE
            )
        } else {
            initializeRecorder()
        }

        fab = findViewById(R.id.fab)
        bottomAppBar = findViewById(R.id.bottomapp_bar)

        fab.setOnClickListener {
           if (!isRecording) {
                showRecordingDialog()
                startRecording()

            } else {
                stopRecording()
                dismissRecordingDialog()
            }
        }

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_home) as NavHostFragment
        navController =navHostFragment.navController
        val navView: BottomNavigationView = binding.navView

        setupWithNavController(navView, navController)
        getFragment()
    }

    private fun getFragment(){
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

    private fun initializeRecorder() {
        mediaRecorder = MediaRecorder()
        mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS)
        mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeRecorder()
            } else {
                // Handle permission denied
            }
        }
    }

    private fun startRecording() {
        if (!isRecording) {
            // Hentikan rekaman jika sedang berjalan
            stopRecording()

            // buat instance baru MediaRecorder setiap kali mulai merekam
            mediaRecorder = MediaRecorder()

            // buat nama file rekaman baru
            currentRecordingFileName = "${externalCacheDir?.absolutePath}/recording_${System.currentTimeMillis()}.wav"
            mediaRecorder?.setOutputFile(currentRecordingFileName)

            // inisialisasi dan mulai rekaman
            mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
            mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS)
            mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            mediaRecorder?.prepare()
            mediaRecorder?.start()

            isRecording = true
        }
        fab.setImageResource(R.drawable.baseline_mic_off_24) // Change icon to stop icon
    }

    private fun stopRecording() {
        if (isRecording) {
            try {
                mediaRecorder?.stop()
            } catch (e: IllegalStateException) {
                // Tangkap IllegalStateException yang mungkin terjadi jika MediaRecorder tidak dalam keadaan yang benar
                e.printStackTrace()
            }
            mediaRecorder?.release()
            mediaRecorder = null

            isRecording = false
            fab.setImageResource(R.drawable.baseline_mic_none_72) // Change icon back to mic icon
        }
    }

    private fun uploadRecording() {
        if (currentRecordingFileName != null) {
            val audioFile = File(currentRecordingFileName)
            viewModel.uploadAudio(audioFile)
                .observe(this@MainActivity) { result ->
                    Log.d("AudioRecording", "Upload audio result: $result")
                    when (result) {
                        is Result.Loading -> {
                            showLoading(true)
                        }

                        is Result.Success -> {
                            showLoading(false)
                            val recordUploadResponse = result.data
                            if (recordUploadResponse != null && recordUploadResponse.data != null) {
                                val randomResult = recordUploadResponse.data.randomResult

                                if (randomResult != null) {
                                    val predictionText = randomResult.predictionText.predictionText
                                    val additionalInfo = randomResult.predictionText.additionalInfo

                                    val intent = Intent(
                                        this@MainActivity,
                                        ResultActivity::class.java
                                    ).apply {
                                        putExtra(ResultActivity.EXTRA_PREDICTION_TEXT, predictionText)
                                        putExtra(ResultActivity.EXTRA_SUGGESTION_1, additionalInfo.suggestion1)
                                        putExtra(ResultActivity.EXTRA_SUGGESTION_2, additionalInfo.suggestion2)
                                        putExtra(ResultActivity.EXTRA_SUGGESTION_3, additionalInfo.suggestion3)
                                    }
                                    startActivity(intent)
                                } else {
                                    showToast("Random result is null.")
                                }
                            } else {
                                showToast("Record upload response or its data is null.")
                            }
                            shouldLaunchResultActivity = true
                        }

                        is Result.Error -> {
                            showToast(result.error ?: "Unknown error occurred")
                            retryUploadRecording()
                            showLoading(true)
                        }
                    }
                }
        } else {
           showToast("Belum ada rekaman valid")
        }
    }
    private fun retryUploadRecording() {
        // Add your retry logic here, for example, using a delay before retrying
        Handler(Looper.getMainLooper()).postDelayed({
            uploadRecording()
        }, 2000) // Retry after a 5-second delay, adjust as needed
    }

    private fun showRecordingDialog() {
        val dialogView = layoutInflater.inflate(R.layout.recording_dialog, null)
        val timerTextView: TextView = dialogView.findViewById(R.id.timeTextView)
        val cancelButton: Button = dialogView.findViewById(R.id.cancelButton)

        recordingDialog = androidx.appcompat.app.AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        recordingDialog?.show()
        isDialogShowing = true

        object : CountDownTimer(10000, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000
                timerTextView.text = "Recording: $seconds seconds left"
            }

            override fun onFinish() {
                stopRecording()
                isDialogShowing = false

                if (currentRecordingFileName != null) {
                    uploadRecording()
                }

                recordingDialog?.dismiss()
            }
        }.start()

        cancelButton.setOnClickListener {
            // Batalkan rekaman
            stopRecording()

            // Hapus file rekaman jika sudah ada
            currentRecordingFileName?.let {
                val file = File(it)
                if (file.exists()) {
                    file.createNewFile()
                }
            }

            recordingDialog?.dismiss()
            isDialogShowing = false

            uploadRecording()
        }
    }

    private fun dismissRecordingDialog() {
        recordingDialog?.dismiss()
    }
    private fun showLoading(isLoading: Boolean) {
        binding.fab.isEnabled = !isLoading
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopRecording()
        if (isDialogShowing) {
            recordingDialog?.dismiss()
        }
        mediaRecorder?.release()
    }

}