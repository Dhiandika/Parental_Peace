package com.dicoding.parentalpeaceapp.response

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class DoctorResponse(

	@field:SerializedName("data")
	val data: List<DataDoctorItem>,

	@field:SerializedName("status")
	val status: String
)

@Parcelize
@Entity
data class DataDoctorItem(

	@PrimaryKey
	@field:SerializedName("doctorId")
	val doctorId: String,

	@field:SerializedName("rate")
	val rate: String,

	@field:SerializedName("Harga")
	val harga: String,

	@field:SerializedName("imageUrl")
	val imageUrl: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("specialis")
	val specialis: String,

	@field:SerializedName("status")
	val status: String
) : Parcelable
