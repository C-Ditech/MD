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
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        val penyakit = responseBody.Result.penyakit
                        val akurasi = responseBody.Result.akurasi

                        println("Penyakit: $penyakit")
                        println("Akurasi: $akurasi")
                    } else {
                        println("Response unsuccessful: Error response from the server")
                    }
                } else {
                    println("Response unsuccessful: ${response.code()} ${response.message()}")
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