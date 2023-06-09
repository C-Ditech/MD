package com.example.mycapstone.ui.Login

import android.util.Log
import androidx.lifecycle.*
import com.example.mycapstone.*
import com.example.mycapstone.api.ApiConfig
import com.example.mycapstone.api.LoginResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel (private val pref: UserPreference) : ViewModel(){

    private val _login = MutableLiveData<LoginResponse>()

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    var isError: Boolean = false

    fun LoginData(email:String, password:String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().uploadDataLogin(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                _isLoading.value = false
                val responseBody = response.body()
                if (response.isSuccessful) {
                    if (responseBody!= null && !responseBody.error){
                        val token = responseBody.loginResult.token
                        val name = responseBody.loginResult.name
                        saveUserData(UserToken(token))
                        saveUsername(UserID(name))
                    }
                    isError = false
                    Log.e("loginResponse", "onResponse: ${response.message()}")
                    _message.value = responseBody?.message.toString()
                    _login.value = response.body()
                } else {
                    isError = true
                    _message.value = response.message()
                    Log.e("regis", "onResponse: ${response.message()} / akun ready")
                }
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("loginFailure", "onFailure: ${t.message}")
            }
        })
    }
    fun PostLoginData(email: String, password: String) {
        viewModelScope.launch {
            LoginData(email, password)
            EmailData(email,password)
        }
    }




    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun login() {
        viewModelScope.launch {
            pref.login()
        }
    }

    fun saveUserData(userData: UserToken){
        viewModelScope.launch {
            pref.saveUserToken(userData)
        }
    }

    fun saveUsername(userData: UserID){
        viewModelScope.launch {
            pref.saveUserID(userData)
        }
    }

    fun EmailData(email:String, password:String){
        val userEmail = UserEmail(email, password)
        saveEmail(userEmail)
    }
    fun saveEmail(userData: UserEmail){
        viewModelScope.launch {
            pref.saveUserEmail(userData)
        }
    }

}