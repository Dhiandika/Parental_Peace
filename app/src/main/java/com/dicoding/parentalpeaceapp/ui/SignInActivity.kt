package com.dicoding.parentalpeaceapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.dicoding.parentalpeaceapp.MainActivity
import com.dicoding.parentalpeaceapp.R

class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        val loginButton: ImageButton = findViewById(R.id.btn_signin)
        val clickableTextView: TextView = findViewById(R.id.tv_signup_click)

        loginButton.setOnClickListener {
            // Tindakan yang akan dilakukan saat SignIn ImageButton diklik
            // Misalnya, buka aktivitas SignInActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

            showToast("Sign In Succes")
        }

        clickableTextView.setOnClickListener {

            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}