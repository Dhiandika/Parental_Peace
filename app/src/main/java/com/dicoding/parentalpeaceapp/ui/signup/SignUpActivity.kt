package com.dicoding.parentalpeaceapp.ui.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.dicoding.parentalpeaceapp.R
import com.dicoding.parentalpeaceapp.databinding.ActivitySignUpBinding
import com.dicoding.parentalpeaceapp.result.Result
import com.dicoding.parentalpeaceapp.ui.signin.SignInActivity
import com.dicoding.parentalpeaceapp.ui.ViewModelFactory

class SignUpActivity : AppCompatActivity() {

    private val viewModel by viewModels<SignUpViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        showLoading(false)
        setupAction()

        binding.tvSigninClick.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        binding.backButton1.setOnClickListener {
            finish()
        }
    }

    private fun setupAction() {
        binding.btnCrtAccount.setOnClickListener {
            showLoading(true)
            val name = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()
            val phone = binding.etNumber.text.toString()
            val password = binding.etPassword.text.toString()
            val retypePw = binding.etRetypepw.text.toString()

            when {
                name.isEmpty() -> {
                    showLoading(false)
                    binding.etName.error = getString(R.string.null_name)
                }
                email.isEmpty() -> {
                    showLoading(false)
                    binding.etEmail.error = getString(R.string.null_email)
                }
                phone.isEmpty() -> {
                    showLoading(false)
                    binding.etNumber.error = getString(R.string.null_number)
                }
                password.isEmpty() -> {
                    showLoading(false)
                    binding.etPassword.error = getString(R.string.null_password)
                }
                retypePw.isEmpty() -> {
                    showLoading(false)
                    binding.etRetypepw.error = getString(R.string.null_password)
                }
                retypePw != password -> {
                    showLoading(false)
                    binding.etRetypepw.error = getString(R.string.not_simmilar)
                }
                else -> {
                    register(name, email, phone, password)
                }
            }
        }
    }

    private fun register(name: String, email: String, phone: String, password: String){
        showLoading(true)
        viewModel.register(name, email, phone, password).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        showLoading(true)

                    }
                    is Result.Success -> {
                        showLoading(true)
                        Toast.makeText(this, "Account Created Succesfully", Toast.LENGTH_SHORT).show()
                        viewModel.getSession()
                        startActivity(Intent(this, SignInActivity::class.java))
                    }
                    is Result.Error -> {
                        showLoading(false)
                        Toast.makeText(this, "Sign Up Failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun showDialog(){
        AlertDialog.Builder(this@SignUpActivity).apply {
            setTitle("Nice!")
            setMessage(getString(R.string.regis_succses))
            setPositiveButton(getString(R.string.next)) { _, _ ->
                val intent = Intent(context, SignInActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
            create()
            setCancelable(false)
        }.show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.btnCrtAccount.isEnabled = !isLoading
        binding.tvSigninClick.isEnabled = !isLoading
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}