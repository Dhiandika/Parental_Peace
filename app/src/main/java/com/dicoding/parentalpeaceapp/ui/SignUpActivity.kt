package com.dicoding.parentalpeaceapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.dicoding.parentalpeaceapp.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

@Suppress("DEPRECATION", "SameParameterValue")
class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.btnCrtAccount.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (email.isEmpty()){
                binding.etEmail.error = "Please fill in your email"
                binding.etEmail.requestFocus()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                binding.etEmail.error = "Invalid email"
                binding.etEmail.requestFocus()
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

            RegisterFirebase(email,password)

        }

        binding.tvSigninClick.setOnClickListener {
            // Tindakan yang akan dilakukan saat TextView diklik
            // Misalnya, buka aktivitas SignInActivity
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        binding.backButton1.setOnClickListener {
            onBackPressed() // Panggil onBackPressed() untuk kembali ke activity sebelumnya
        }
    }

    private fun RegisterFirebase(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(this){
                if (it.isSuccessful){
                    showToast("Account created successfully")
                    val intent = Intent(this, SignInActivity::class.java)
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