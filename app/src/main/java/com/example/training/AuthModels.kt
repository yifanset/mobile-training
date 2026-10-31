package com.example.training

import com.google.gson.annotations.SerializedName

// Запрос оставляем без изменений
data class LoginRequest(
    val email: String,
    val password: String
)

// Обновленный ответ
data class LoginResponse(
    @SerializedName("accessToken") val accessToken: String,
    @SerializedName("refreshToken") val refreshToken: String,
    @SerializedName("role") val role: String
)

// 1. Запрос на регистрацию
data class RegisterRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val role: String // Например, "CLIENT" или "TRAINER"
)

// 2. Ответ на регистрацию (сервер может вернуть просто сообщение)
data class RegisterResponse(
    val message: String? = null,
    val email: String? = null // Иногда сервер возвращает email обратно
)

// 3. Запрос на подтверждение кода
data class VerifyEmailRequest(
    val email: String,
    val code: String
)

// 4. Ответ на подтверждение (может вернуть токены, как при логине, или просто ОК)
data class VerifyEmailResponse(
    val message: String? = null
)