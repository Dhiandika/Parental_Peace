package com.dicoding.parentalpeaceapp.ui.forgot

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.dicoding.parentalpeaceapp.R
import com.dicoding.parentalpeaceapp.databinding.ActivityForgotPwBinding
import com.dicoding.parentalpeaceapp.result.Result
import com.dicoding.parentalpeaceapp.ui.ViewModelFactory

class ForgotPwActivity : AppCompatActivity() {

    private val viewModel by viewModels<ForgotViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityForgotPwBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityForgotPwBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        showLoading(false)
        setupAction()

    }

    private fun setupAction() {
        binding.btnForgot.setOnClickListener {
            val email = binding.etEmailLogin.text.toString()

            if (email.isEmpty()) {
                showToast(getString(R.string.null_email))
                return@setOnClickListener
            }else{
                forgot(email)
            }
        }
    }

    private fun forgot(email: String) {
        viewModel.forgot(email).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        showLoading(true)

                    }
                    is Result.Success -> {
                        showLoading(false)
                        // Tampilkan dialog dengan pesan "Email is valid"
                        showDialog("Email is valid", result.data.resetLink)

                    }
                    is Result.Error -> {
                        showLoading(false)
                        Toast.makeText(this, "Change Passwordfailed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun showDialog(message: String, resetLink: String) {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage(message)
            .setCancelable(false)
            .setPositiveButton("Continue") { dialog, _ ->
                // Tambahkan logika untuk menghandle klik tombol Continue
                openBrowserWithLink(resetLink)
                dialog.dismiss()
            }

        val alert = dialogBuilder.create()
        alert.show()
    }

    private fun openBrowserWithLink(link: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        startActivity(browserIntent)
    }

    private fun showToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.btnForgot.isEnabled = !isLoading
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}