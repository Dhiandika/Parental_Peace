package com.dicoding.parentalpeaceapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.dicoding.parentalpeaceapp.R

@Suppress("DEPRECATION", "SameParameterValue")
class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val createAccountButton: ImageButton = findViewById(R.id.btn_crtAccount)
        val clickableTextView: TextView = findViewById(R.id.tv_signin_click)

        createAccountButton.setOnClickListener {
            // Tindakan yang akan dilakukan saat SignIn ImageButton diklik
            // Misalnya, buka aktivitas SignInActivity
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)

            showToast("Account created successfully")
        }

        clickableTextView.setOnClickListener {
            // Tindakan yang akan dilakukan saat TextView diklik
            // Misalnya, buka aktivitas SignInActivity
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        val backButton: ImageButton = findViewById(R.id.back_button1)

        backButton.setOnClickListener {
            onBackPressed() // Panggil onBackPressed() untuk kembali ke activity sebelumnya
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}