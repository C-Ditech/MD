package com.example.mycapstone.ui.upload

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mycapstone.api.ApiConfig
import com.example.mycapstone.api.HasilResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UploadViewModel : ViewModel() {

    val result = MutableLiveData<Result<HasilResponse>>()
    private val _disease = MutableLiveData<String>()
    private val _accuracy = MutableLiveData<Double>()
    val disease: LiveData<String> = _disease
    val accuracy: LiveData<Double> = _accuracy
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    fun cekpenyakit( imageMultipart: MultipartBody.Part): LiveData<Result<HasilResponse>> {
        _isLoading.value = true
        val service = ApiConfig.getApiService2().cekpenyakit(imageMultipart)
        service.enqueue(object : Callback<HasilResponse> {
            override fun onResponse(call: Call<HasilResponse>, response: Response<HasilResponse>) {
                _isLoading.value = false
                _isError.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        val penyakit = responseBody.Result.penyakit
                        val akurasi = responseBody.Result.akurasi

                        println("Penyakit: $penyakit")
                        println("Akurasi: $akurasi")

                        _disease.value = penyakit
                        _accuracy.value = akurasi

                    } else {
                        _isError.value = true
                        println("Response unsuccessful: Error response from the server")

                    }
                } else {
                    _isError.value = true
                    println("Response unsuccessful: ${response.code()} ${response.message()}")
                }
            }


            override fun onFailure(call: Call<HasilResponse>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
            }
        })

        return result
    }
}