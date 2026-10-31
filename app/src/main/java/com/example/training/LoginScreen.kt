package com.example.training

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onBackClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    // Меняем переменную phone на email
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Состояние загрузки (чтобы показать прогресс-бар)
    var isLoading by remember { mutableStateOf(false) }

    // Цвета из дизайна
    val textColor = Color.White
    val borderColor = Color.White
    val linkColor = Color(0xFF7AA8FF)
    val hintColor = Color(0xFF888888)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF1A1512), Color(0xFF0F0D0B))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(100.dp))

            Text(
                text = "Вход",
                color = textColor,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(60.dp))

            // ПОЛЕ EMAIL (вместо телефона)
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Email",
                    color = textColor,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text("example@mail.ru", color = hintColor) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = borderColor,
                        unfocusedBorderColor = borderColor,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = textColor,
                        unfocusedTextColor = textColor,
                        cursorColor = textColor
                    ),
                    // Меняем тип клавиатуры на Email
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Поле пароля (без изменений)
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Пароль",
                    color = textColor,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = { Text("Пароль", color = hintColor) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = borderColor,
                        unfocusedBorderColor = borderColor,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = textColor,
                        unfocusedTextColor = textColor,
                        cursorColor = textColor
                    ),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Ссылки
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(
                    onClick = { /* Логика восстановления */ },
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("Забыли пароль", color = linkColor, fontSize = 14.sp)
                }

                TextButton(
                    onClick = onRegisterClick,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("Регистрация", color = linkColor, fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Кнопка "Далее"
            if (isLoading) {
                // Если идет загрузка, показываем крутящийся индикатор
                CircularProgressIndicator(color = Color(0xFFFFAE25))
                Spacer(modifier = Modifier.height(48.dp)) // Компенсируем высоту кнопки
            } else {
                Button(
                    onClick = {
                        // Проверка на пустые поля (чтобы не отправлять пустоту)
                        if (email.isBlank() || password.isBlank()) {
                            Log.e("API_TEST", "Email или пароль пустые!")
                            return@Button
                        }

                        isLoading = true // Включаем загрузку

                        // 1. Формируем запрос
                        val request = LoginRequest(email = email, password = password)

                        // 2. Отправляем его
                        RetrofitClient.apiService.login(request).enqueue(object : Callback<LoginResponse> {
                            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                                isLoading = false

                                if (response.isSuccessful) {
                                    val body = response.body()
                                    if (body != null) {
                                        Log.d("API_TEST", "Успешный вход!")
                                        Log.d("API_TEST", "Роль пользователя: ${body.role}")
                                        Log.d("API_TEST", "Access Token: ${body.accessToken.take(20)}...") // Показываем только начало токена (он длинный)

                                        // TODO: Здесь мы позже сохраним токен в DataStore или SharedPreferences

                                        onLoginSuccess() // Переходим на главный экран
                                    }
                                } else {
                                    Log.e("API_TEST", "Ошибка: Код ${response.code()}, Ответ: ${response.errorBody()?.string()}")
                                }
                            }

                            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                                isLoading = false // Выключаем загрузку
                                // Ошибка сети (нет интернета, сервер упал)
                                Log.e("API_TEST", "Сетевая ошибка: ${t.message}", t)
                            }
                        })
                    },
                    modifier = Modifier
                        .width(140.dp)
                        .height(48.dp)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(Color(0xFFFFAE25), Color(0xFFFF6417))
                            ),
                            shape = RoundedCornerShape(24.dp)
                        ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text(
                        text = "Далее",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}