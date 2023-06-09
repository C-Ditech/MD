package com.example.mycapstone

data class UserModel(
    val name: String,
    val email: String,
    val password: String,
    val isLogin: Boolean
)

data class UserID(
    val name: String
)

data class UserEmail(
    val email: String,
    val password: String,
)
data class UserToken (
    val token: String
)