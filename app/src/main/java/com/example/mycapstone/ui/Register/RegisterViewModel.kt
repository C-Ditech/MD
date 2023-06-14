package com.example.mycapstone.ui.Register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mycapstone.UserModel
import com.example.mycapstone.UserPreference
import com.example.mycapstone.api.ApiConfig
import com.example.mycapstone.api.RegisterResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel (private val pref: UserPreference)   : ViewModel() {

    private val _regis = MutableLiveData<RegisterResponse>()

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    var isError: Boolean = false



    fun RegrisData(name: String, email:String, password:String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().uploadRegister(name, email, password)
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                _isLoading.value = false
                val responseBody = response.body()
                if (response.isSuccessful) {
                    isError = false
                    Log.e("regisResponse", "onResponse: ${response.message()}")
                    _message.value = responseBody?.email.toString()
                    _regis.value = response.body()
                } else {
                    isError = true
                    _message.value = response.message()
                    Log.e("regis", "onResponse: ${response.message()} / akun ready")
                }
            }
            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Log.e("regisFailure", "onFailure: ${t.message}")
                _isLoading.value = false
            }
        })
    }

    fun postDataRegister(name: String, email: String, password: String) {
        viewModelScope.launch {
            RegrisData(name, email, password)
        }
    }

    fun saveUser(user: UserModel) {
        viewModelScope.launch {
            pref.saveUser(user)
        }
    }

}