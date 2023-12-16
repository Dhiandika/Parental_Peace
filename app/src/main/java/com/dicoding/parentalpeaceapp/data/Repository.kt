package com.dicoding.parentalpeaceapp.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.dicoding.parentalpeaceapp.data.database.ParentalPeaceDatabase
import com.dicoding.parentalpeaceapp.response.ArticleResponse
import com.dicoding.parentalpeaceapp.response.DataItem
import com.dicoding.parentalpeaceapp.retrofit.ApiService
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response

class Repository(private val database: ParentalPeaceDatabase, private val apiService: ApiService) {
    private var _listArticle = MutableLiveData<List<DataItem>?>()
    var listArticle: MutableLiveData<List<DataItem>?> = _listArticle

    fun listArticle() {
        val client = apiService.getArticle()
        client.enqueue(object : Callback<ArticleResponse> {
            override fun onResponse(
                call: Call<ArticleResponse>,
                response: Response<ArticleResponse>
            ) {
                if (response.isSuccessful) {
                    _listArticle.value = response.body()?.data
                }
            }

            override fun onFailure(call: Call<ArticleResponse>, t: Throwable) {
                Log.e("Repository", "error: ${t.message}")
            }
        })
    }


    companion object {
        @Volatile
        private var instance: Repository? = null

        fun getInstance(
            database: ParentalPeaceDatabase,
            apiService: ApiService
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(database, apiService)
            }.also { instance = it }

    }
}