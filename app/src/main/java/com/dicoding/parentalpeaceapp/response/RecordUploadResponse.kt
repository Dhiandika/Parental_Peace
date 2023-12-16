package com.dicoding.parentalpeaceapp.response

import com.google.gson.annotations.SerializedName

data class RecordUploadResponse(

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("status")
	val status: String
)

data class Data(

	@field:SerializedName("dataResponse")
	val dataResponse: DataResponse,

	@field:SerializedName("randomResult")
	val randomResult: RandomResult
)

data class PredictionText(

	@field:SerializedName("additional_info")
	val additionalInfo: AdditionalInfo,

	@field:SerializedName("prediction_text")
	val predictionText: String
)

data class AdditionalInfo(

	@field:SerializedName("suggestion1")
	val suggestion1: String,

	@field:SerializedName("suggestion3")
	val suggestion3: String,

	@field:SerializedName("suggestion2")
	val suggestion2: String
)

data class DataResponse(

	@field:SerializedName("predicted_class")
	val predictedClass: String
)

data class RandomResult(

	@field:SerializedName("prediction_id")
	val predictionId: String,

	@field:SerializedName("prediction_text")
	val predictionText: PredictionText
)
