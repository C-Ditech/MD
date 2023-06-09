package com.example.mycapstone.api

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class RegisterResponse(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)

data class LoginResponse(

    val loginResult: LoginResult,
    val error: Boolean,
    val message: String
)



data class LoginResult(
    val name: String,
    val userId: String,
    val token: String
)



