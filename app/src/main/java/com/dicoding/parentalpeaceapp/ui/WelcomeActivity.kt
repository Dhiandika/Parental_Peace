package com.dicoding.parentalpeaceapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import com.dicoding.parentalpeaceapp.R

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val signInButton: ImageButton = findViewById(R.id.signin_button)
        val signUpButton: ImageButton = findViewById(R.id.signup_button)

        signInButton.setOnClickListener {
            // Tindakan yang akan dilakukan saat SignIn ImageButton diklik
            // Misalnya, buka aktivitas SignInActivity
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        signUpButton.setOnClickListener {
            // Tindakan yang akan dilakukan saat SignUp ImageButton diklik
            // Misalnya, buka aktivitas SignUpActivity
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}