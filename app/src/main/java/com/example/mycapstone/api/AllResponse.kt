package com.example.mycapstone.api

data class RegisterResponse(

    val email: Boolean,
    val name: String,
    val password: String
)

data class LoginResponse(
    val token: String,
    val user: LoginResult

)


data class LoginResult(
    val name: String,
    val email: String,
)

data class HasilResponse(
    val error: Boolean,
    val message: String,
    val Result: Result
)

data class Result(
    val penyakit: String,
    val akurasi: Double,

)


