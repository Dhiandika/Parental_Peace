package com.dicoding.parentalpeaceapp.response

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class ArticleResponse(

	@field:SerializedName("data")
	val data: List<DataItem>?,

	@field:SerializedName("status")
	val status: String?
)

@Parcelize
@Entity
data class DataItem(

	@PrimaryKey
	@field:SerializedName("articleId")
	val articleId: String,

	@field:SerializedName("article_picture")
	val articlePicture: String?,

	@field:SerializedName("title")
	val title: String?,

	@field:SerializedName("content")
	val content: String?
) : Parcelable
