package com.dicoding.parentalpeaceapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.dicoding.parentalpeaceapp.data.database.ParentalPeaceDatabase
import com.dicoding.parentalpeaceapp.response.ArticleResponse
import com.dicoding.parentalpeaceapp.response.DataDoctorItem
import com.dicoding.parentalpeaceapp.response.DataItem
import com.dicoding.parentalpeaceapp.response.DoctorResponse
import com.dicoding.parentalpeaceapp.response.ForgotPasswordResponse
import com.dicoding.parentalpeaceapp.response.RecordUploadResponse
import com.dicoding.parentalpeaceapp.response.SignInResponse
import com.dicoding.parentalpeaceapp.response.SignUpResponse
import com.dicoding.parentalpeaceapp.result.Result
import com.dicoding.parentalpeaceapp.retrofit.ApiConfig
import com.dicoding.parentalpeaceapp.retrofit.ApiService
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Callback
import retrofit2.Call
import retrofit2.HttpException
import retrofit2.Response
import java.io.File
import java.net.SocketTimeoutException

class Repository(
    private val database: ParentalPeaceDatabase,
    private val userPreference: UserPreference,
    private val apiService: ApiService) {

    private var _listArticle = MutableLiveData<List<DataItem>?>()
    var listArticle: MutableLiveData<List<DataItem>?> = _listArticle

    private var _listDoctor = MutableLiveData<List<DataDoctorItem>>()
    var listDoctor: MutableLiveData<List<DataDoctorItem>> = _listDoctor

    fun register(
        username: String,
        email: String,
        phone: String,
        password: String
    ): LiveData<Result<SignUpResponse>> = liveData {
        emit(Result.Loading)
        try {
            val registerResponse = apiService.register(username, email, phone, password)
            if (registerResponse.error == false) {
                emit(Result.Success(registerResponse))
            } else {
                emit(Result.Error(registerResponse.message))
            }
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, SignUpResponse::class.java)
            val errorMessage = errorBody?.message ?: "An error occurred"
            emit(Result.Error("Registration failed: ${e.message}"))
        } catch (e: Exception) {
            emit(Result.Error("Internet Issues"))
        }
    }


    fun signIn(
        email: String,
        password: String
    ) = liveData {
        emit(Result.Loading)
            try {
                val responseLogin = apiService.login(email, password)
                if (responseLogin.error == false) {
                    val token = UserModel(
                        name = responseLogin.loginResult.name,
                        email = responseLogin.loginResult.email,
                        phone = responseLogin.loginResult.phone,
                        userId = responseLogin.loginResult.userId,
                        token = responseLogin.loginResult.token,
                        isLogin = true
                    )
                    ApiConfig.token = responseLogin.loginResult.token
                    userPreference.saveSession(token)
                    emit(Result.Success(responseLogin))
                } else {
                    emit(Result.Error(responseLogin.message))
                }
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, SignInResponse::class.java)
                val errorMessage = errorBody?.message ?: "An error occurred"
                emit(Result.Error("Login failed: $errorMessage")) // Mengirimkan hasil error
            } catch (e: Exception) {
                emit(Result.Error("Internet Issues"))
            }
    }

    fun forgot(
        email: String
    ): LiveData<Result<ForgotPasswordResponse>> = liveData {
        emit(Result.Loading)
        try {
            val forgotResponse = apiService.forgot(email)
            if (forgotResponse.status == "success") {
                emit(Result.Success(forgotResponse))
            } else {
                emit(Result.Error(forgotResponse.message))
            }
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ForgotPasswordResponse::class.java)
            val errorMessage = errorBody?.message ?: "An error occurred"
            emit(Result.Error("Change password failed: ${e.message}"))
        } catch (e: Exception) {
            emit(Result.Error("Internet Issues"))
        }
    }

    fun uploadAudio(audioFile: File)  = liveData {
        emit(Result.Loading)
        val requestAudioFile = audioFile.asRequestBody("audio/acc".toMediaTypeOrNull())
        val multipartBody = MultipartBody.Part.createFormData(
            "audio",
            audioFile.name,
            requestAudioFile
        )
        try {
            val successResponse = apiService.uploadAudio(multipartBody)
            emit(Result.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, RecordUploadResponse::class.java)
            emit(Result.Error(errorResponse.status))
        } catch (e: SocketTimeoutException) {
            // Handle timeout exception
            emit(Result.Error("Connection timeout. Please Wait a Moment."))
        }
    }

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

    fun listDoctor() {
        val client = apiService.getDoctor()
        client.enqueue(object : Callback<DoctorResponse> {
            override fun onResponse(
                call: Call<DoctorResponse>,
                response: Response<DoctorResponse>
            ) {
                if (response.isSuccessful) {
                    _listDoctor.value = response.body()?.data
                }
            }

            override fun onFailure(call: Call<DoctorResponse>, t: Throwable) {
                Log.e("Repository", "error: ${t.message}")
            }
        })
    }

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: Repository? = null

        fun getInstance(
            database: ParentalPeaceDatabase,
            userPreference: UserPreference,
            apiService: ApiService
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(database, userPreference, apiService)
            }.also { instance = it }

        fun clearInstance() {
            instance = null
        }
    }
}