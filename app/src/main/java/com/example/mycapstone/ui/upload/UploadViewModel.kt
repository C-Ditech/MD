package com.example.mycapstone.ui.upload

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mycapstone.UserID
import com.example.mycapstone.UserToken
import com.example.mycapstone.api.ApiConfig
import com.example.mycapstone.api.HasilResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UploadViewModel : ViewModel() {
    val result = MutableLiveData<Result<HasilResponse>>()

    fun cekpenyakit( imageMultipart: MultipartBody.Part): LiveData<Result<HasilResponse>> {
//        _isLoading.value = true
        val service = ApiConfig.getApiService2().cekpenyakit(imageMultipart)
        service.enqueue(object : Callback<HasilResponse> {
            override fun onResponse(call: Call<HasilResponse>, response: Response<HasilResponse>) {
//                _isLoading.value = false
                val responseBody = response.body()
                if (response.isSuccessful) {
                    if (responseBody!= null ){
//                        val token = responseBody.loginResult.token
//                        saveUserData(UserToken(token))
                        val penyakit = responseBody.Result.penyakit
                        val akurasi = responseBody.Result.akurasi

                        println("Penyakit: $penyakit")
                        println("Akurasi: $akurasi")
                    }
//                    isError = false
                    Log.e("loginResponse", "onResponse: ${response.message()}")
//                    _message.value = responseBody?.message.toString()
//                    _login.value = response.body()
                } else {
//                    isError = true
//                    _message.value = response.message()
                    println("Response unsuccessful")
                }
            }

            override fun onFailure(call: Call<HasilResponse>, t: Throwable) {
//                _isLoading.value = false
                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
            }
        })

        return result
    }
}