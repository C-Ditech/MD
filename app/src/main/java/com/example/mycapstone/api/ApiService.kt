package com.example.mycapstone.api

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("sign-up")
    fun uploadRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("sign-in")
    fun uploadDataLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @Multipart
    @POST("predict_image")
    fun cekpenyakit(@Part uploaded_file: MultipartBody.Part): Call<HasilResponse>

}