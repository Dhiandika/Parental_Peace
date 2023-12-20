package com.dicoding.parentalpeaceapp.ui.information

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.parentalpeaceapp.R
import com.dicoding.parentalpeaceapp.adapter.ArticlesListAdapter
import com.dicoding.parentalpeaceapp.databinding.ActivityInformationBinding
import com.dicoding.parentalpeaceapp.response.DataItem
import com.dicoding.parentalpeaceapp.ui.ViewModelFactory

class InformationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInformationBinding
    private val viewModel by viewModels<InformationViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showRv()

        viewModel.listArticle().observe(this) { articles ->
            if (articles != null) {
                setArticleList(articles)
            } else {
                showToast(getString(R.string.no_articel))
            }
        }
    }

    private fun showRv() {
        val layoutManager = LinearLayoutManager(this)
        binding.rvInformatioArticle.layoutManager = layoutManager
    }

    private fun setArticleList(articles: List<DataItem>) {
        val adapter = ArticlesListAdapter()
        adapter.submitList(articles)
        binding.rvInformatioArticle.adapter = adapter
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}