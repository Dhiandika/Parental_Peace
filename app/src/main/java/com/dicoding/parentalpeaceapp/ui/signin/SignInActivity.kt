package com.dicoding.parentalpeaceapp.ui.signin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.dicoding.parentalpeaceapp.R
import com.dicoding.parentalpeaceapp.data.UserModel
import com.dicoding.parentalpeaceapp.databinding.ActivitySignInBinding
import com.dicoding.parentalpeaceapp.result.Result
import com.dicoding.parentalpeaceapp.ui.forgot.ForgotPwActivity
import com.dicoding.parentalpeaceapp.ui.main.MainActivity
import com.dicoding.parentalpeaceapp.ui.ViewModelFactory
import com.dicoding.parentalpeaceapp.ui.signup.SignUpActivity

class SignInActivity : AppCompatActivity() {

    private val viewModel by viewModels<SignInViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySignInBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        showLoading(false)

        setupAction()

        binding.tvSignupClick.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.forgotPassword.setOnClickListener {
            val intent = Intent(this, ForgotPwActivity::class.java)
            startActivity(intent)
        }
    }


    private fun setupAction() {
        binding.btnSignin.setOnClickListener {
            val email = binding.etEmailLogin.text.toString()
            val password = binding.etPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                showToast(getString(R.string.null_email))
                return@setOnClickListener
            }else{
                signin(email, password)
            }
        }
    }

    private fun signin(email: String, password: String) {
        viewModel.signin(email, password).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        showLoading(true)

                    }
                    is Result.Success -> {
                        showLoading(false)
                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                        Toast.makeText(this, "SignIn Success", Toast.LENGTH_SHORT).show()
                        val userData = UserModel(
                            result.data.loginResult.name,
                            result.data.loginResult.email,
                            result.data.loginResult.phone,
                            result.data.loginResult.userId,
                            result.data.loginResult.token,
                            true)
                        viewModel.saveSession(userData)
                        finish()
                    }
                    is Result.Error -> {
                        showLoading(false)
                        Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun showToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.btnSignin.isEnabled = !isLoading
        binding.tvSignupClick.isEnabled = !isLoading
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}