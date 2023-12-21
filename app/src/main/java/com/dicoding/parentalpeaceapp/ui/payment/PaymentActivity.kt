package com.dicoding.parentalpeaceapp.ui.payment

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.dicoding.parentalpeaceapp.databinding.ActivityPaymentBinding
import com.dicoding.parentalpeaceapp.response.DataDoctorItem

@Suppress("DEPRECATION")
class PaymentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaymentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val payment = intent.getParcelableExtra<DataDoctorItem>(PAYMENT) as DataDoctorItem
        showDetail(payment)

        binding.paymentButton.setOnClickListener{
            val chatUrl = "https://app.sandbox.midtrans.com/payment-links/1702555519807"
            paymentLink(chatUrl)
        }

        // Menangani intent saat aplikasi dibuka melalui URL scheme
        val data: Uri? = intent.data
        if (data != null) {
            handleDeepLink(data)
        }

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        // Handle the new intent
        val data: Uri? = intent?.data
        if (data != null) {
            handleDeepLink(data)
        }
    }

    private fun showDetail(doctor: DataDoctorItem) {
        Glide.with(applicationContext)
            .load(doctor.imageUrl)
            .into(binding.ivArticles)
        binding.apply {
            tvName.text = doctor.name
            tvOnline.text = doctor.status
            tvSpesialis.text = doctor.specialis
            tvRating.text = doctor.rate
            tvPrice.text = doctor.harga
        }
    }

    private fun paymentLink(link: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        this.startActivity(browserIntent)
    }

    private fun handleDeepLink(uri: Uri) {
        uri?.let {
            Log.d("DeepLink", "Scheme: ${uri.scheme}, Host: ${uri.host}")
            if (uri.scheme == "myapp" && uri.host == "payment-success") {
                // Lakukan tindakan setelah pembayaran berhasil
                showPaymentSuccessDialog()
            }
        }
    }

    private fun showPaymentSuccessDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("Payment Success")
            .setPositiveButton("Start Consultation") { _, _ ->
                // Use the correct phone number or contact ID
                val phoneNumber = "081111111111"
                openWhatsApp(phoneNumber)
            }
            .setCancelable(false)
            .create()
            .show()
    }

    private fun openWhatsApp(phoneNumber: String) {
        try {
            val uri = Uri.parse("whatsapp://send?phone=$phoneNumber")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        } catch (e: Exception) {
            // Handle exceptions, e.g., if WhatsApp is not installed
            Toast.makeText(this, "WhatsApp is not installed.", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val PAYMENT = "payment"
    }
}