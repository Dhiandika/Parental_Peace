package com.dicoding.parentalpeaceapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.dicoding.parentalpeaceapp.R

class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        val loginButton: ImageButton = findViewById(R.id.btn_signin)
        val clickableTextView: TextView = findViewById(R.id.tv_signup_click)

        loginButton.setOnClickListener {
            // TODO: Lakukan proses autentikasi di sini
            val isAuthenticationSuccessful = performAuthentication()

            if (isAuthenticationSuccessful) {
                // Proses autentikasi berhasil, lanjutkan ke HomeActivity
                val intent = Intent(this, HomeActivity::class.java)
                intent.putExtra("SHOW_FRAGMENT", "HOME_FRAGMENT") // Informasi tambahan
                startActivity(intent)
                showToast("Sign In Success")
            } else {
                // Proses autentikasi gagal, tampilkan pesan kesalahan jika diperlukan
                showToast("Authentication failed. Please check your credentials.")
            }
        }

        clickableTextView.setOnClickListener {
            // Pindah ke SignUpActivity jika tombol diklik
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun performAuthentication(): Boolean {
        // Implementasi proses autentikasi di sini
        // Misalnya, periksa kredensial pengguna dan kembalikan true jika autentikasi berhasil
        // atau false jika gagal
        return true
    }
}