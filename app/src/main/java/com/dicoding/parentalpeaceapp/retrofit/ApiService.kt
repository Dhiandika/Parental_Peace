package com.dicoding.parentalpeaceapp.retrofit

import com.dicoding.parentalpeaceapp.response.ArticleResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("articles")
    fun getArticle(): Call<ArticleResponse>
}