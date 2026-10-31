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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerifyCodeScreen(
    email: String,
    onVerifySuccess: () -> Unit,
    onBackClick: () -> Unit
) {
    var code by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorText by remember { mutableStateOf<String?>(null) }

    val textColor = Color.White
    val borderColor = Color.White
    val hintColor = Color(0xFF888888)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(colors = listOf(Color(0xFF1A1512), Color(0xFF0F0D0B))))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(100.dp))

            Text("Подтверждение", color = textColor, fontSize = 32.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Мы отправили код на почту:\n$email", color = textColor.copy(alpha = 0.7f), fontSize = 14.sp, textAlign = androidx.compose.ui.text.style.TextAlign.Center)

            Spacer(modifier = Modifier.height(40.dp))

            OutlinedTextField(
                value = code,
                onValueChange = { code = it },
                label = { Text("Код из письма", color = hintColor) },
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                isError = errorText != null
            )

            if (errorText != null) {
                Text(errorText!!, color = Color.Red, fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp))
            }

            Spacer(modifier = Modifier.weight(1f))

            if (isLoading) {
                CircularProgressIndicator(color = Color(0xFFFFAE25))
                Spacer(modifier = Modifier.height(48.dp))
            } else {
                Button(
                    onClick = {
                        if (code.isBlank()) return@Button
                        isLoading = true
                        errorText = null

                        val request = VerifyEmailRequest(email = email, code = code)

                        RetrofitClient.apiService.verifyEmail(request).enqueue(object : Callback<VerifyEmailResponse> {
                            override fun onResponse(call: Call<VerifyEmailResponse>, response: Response<VerifyEmailResponse>) {
                                isLoading = false
                                if (response.isSuccessful) {
                                    Log.d("API_TEST", "Почта подтверждена!")
                                    onVerifySuccess() // Перекидываем на экран входа (или сразу в приложение)
                                } else {
                                    errorText = "Неверный код"
                                    Log.e("API_TEST", "Ошибка: ${response.code()}")
                                }
                            }

                            override fun onFailure(call: Call<VerifyEmailResponse>, t: Throwable) {
                                isLoading = false
                                errorText = "Ошибка сети"
                                Log.e("API_TEST", "Сеть: ${t.message}")
                            }
                        })
                    },
                    modifier = Modifier
                        .width(140.dp)
                        .height(48.dp)
                        .background(brush = Brush.linearGradient(colors = listOf(Color(0xFFFFAE25), Color(0xFFFF6417))), shape = RoundedCornerShape(24.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Color.Black),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text("Готово", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}