package com.dicoding.parentalpeaceapp.retrofit

import com.dicoding.parentalpeaceapp.response.ArticleResponse
import com.dicoding.parentalpeaceapp.response.DoctorResponse
import com.dicoding.parentalpeaceapp.response.ForgotPasswordResponse
import com.dicoding.parentalpeaceapp.response.RecordUploadResponse
import com.dicoding.parentalpeaceapp.response.SignInResponse
import com.dicoding.parentalpeaceapp.response.SignUpResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {

    //register
    @FormUrlEncoded
    @POST("users")
    suspend fun register(
        @Field("username") name: String,
        @Field("email") email: String,
        @Field("phone") phone: String,
        @Field("password") password: String
    ): SignUpResponse

    //login
    @FormUrlEncoded
    @POST("loginUsers")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): SignInResponse

    @FormUrlEncoded
    @POST("forgot-password")
    suspend fun forgot(
        @Field("users_email") email: String
    ): ForgotPasswordResponse

    //uploadrecording
    @Multipart
    @POST("predict-audiokon")
    suspend fun uploadAudio(
        @Part audioFile: MultipartBody.Part
    ): RecordUploadResponse

    @GET("articles")
    fun getArticle(): Call<ArticleResponse>
    @GET("getAllDoctors")
    fun getDoctor(): Call<DoctorResponse>
}