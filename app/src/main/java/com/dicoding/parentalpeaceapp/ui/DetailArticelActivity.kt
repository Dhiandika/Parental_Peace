package com.dicoding.parentalpeaceapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.dicoding.parentalpeaceapp.databinding.ActivityDetailArticelBinding
import com.dicoding.parentalpeaceapp.response.DataItem

@Suppress("DEPRECATION")
class DetailArticelActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailArticelBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailArticelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val articles = intent.getParcelableExtra<DataItem>(DETAIL_ARTICLE) as DataItem

        showDetail(articles)
    }

    private fun showDetail(articles: DataItem) {
        Glide.with(applicationContext)
            .load(articles.articlePicture)
            .into(binding.ivDetailArticle)
        binding.apply {
            tittleDetail.text = articles.title
            descDetail.text = articles.content
        }
    }

    companion object {
        const val DETAIL_ARTICLE = "detail_article"
    }
}