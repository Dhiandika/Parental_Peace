package com.dicoding.parentalpeaceapp.ui.consultation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.parentalpeaceapp.R
import com.dicoding.parentalpeaceapp.adapter.DoctorListAdapter
import com.dicoding.parentalpeaceapp.databinding.ActivityConsultationBinding
import com.dicoding.parentalpeaceapp.response.DataDoctorItem
import com.dicoding.parentalpeaceapp.ui.ViewModelFactory

class ConsultationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConsultationBinding
    private val viewModel by viewModels<ConsultationViewModel> {
        ViewModelFactory.getInstance(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConsultationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showRv()

        viewModel.listDoctor().observe(this) { articles ->
            if (articles != null) {
                setArticleList(articles)
            } else {
                showToast(getString(R.string.no_listdoctor))
            }
        }

    }


    private fun showRv() {
        val layoutManager = LinearLayoutManager(this)
        binding.rvListDoctor.layoutManager = layoutManager
    }

    private fun setArticleList(doctor: List<DataDoctorItem>) {
        val adapter = DoctorListAdapter()
        adapter.submitList(doctor)
        binding.rvListDoctor.adapter = adapter
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}