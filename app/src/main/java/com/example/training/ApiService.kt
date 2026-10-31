package com.example.training

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    // Указываем путь относительно базового URL
    @POST("api/v1/auth/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST("api/v1/auth/register")
    fun register(@Body request: RegisterRequest): Call<Void>

    @POST("api/v1/auth/verify-email")
    fun verifyEmail(@Body request: VerifyEmailRequest): Call<VerifyEmailResponse>
}