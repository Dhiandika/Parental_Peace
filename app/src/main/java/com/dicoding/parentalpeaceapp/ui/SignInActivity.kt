package com.dicoding.parentalpeaceapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.dicoding.parentalpeaceapp.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySignInBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.btnSignin.setOnClickListener {
            val email = binding.etEmailLogin.text.toString()
            val password = binding.etPassword.text.toString()

            if (email.isEmpty()){
                binding.etEmailLogin.error = "Please fill in your email"
                binding.etEmailLogin.requestFocus()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                binding.etEmailLogin.error = "Invalid email"
                binding.etEmailLogin.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()){
                binding.etPassword.error = "Please fill in your password"
                binding.etPassword.requestFocus()
                return@setOnClickListener
            }

            if (password.length <= 8){
                binding.etPassword.error = "Password must be at least 8 characters"
                binding.etPassword.requestFocus()
                return@setOnClickListener
            }

            LoginFirebase(email,password)
        }

        binding.tvSignupClick.setOnClickListener {
            // Pindah ke SignUpActivity jika tombol diklik
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun LoginFirebase(email: String, password: String) {
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener(this){
                if (it.isSuccessful){
                    showToast("Sign In Success")
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("SHOW_FRAGMENT", "HOME_FRAGMENT") // Informasi tambahan
                    startActivity(intent)
                } else{
                    showToast("${it.exception?.message}")
                }
            }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    
}