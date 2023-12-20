package com.dicoding.parentalpeaceapp.ui.welcome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import com.dicoding.parentalpeaceapp.R
import com.dicoding.parentalpeaceapp.ui.signin.SignInActivity
import com.dicoding.parentalpeaceapp.ui.signup.SignUpActivity

@Suppress("DEPRECATION")
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

    // Override onBackPressed to exit the app
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity() // Menutup semua activity yang terkait dengan task saat ini
    }
}