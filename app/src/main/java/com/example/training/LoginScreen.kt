package com.example.training

import androidx.compose.foundation.BorderStroke
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onBackClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Цвета из дизайна
    val textColor = Color.White
    val borderColor = Color.White
    val linkColor = Color(0xFF7AA8FF) // Светло-синий для ссылок
    val hintColor = Color(0xFF888888) // Серый для плейсхолдеров

    // Основной контейнер с темным фоном
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                // Имитация темного фона (можно заменить на картинку, если она есть)
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

            // Заголовок
            Text(
                text = "Вход",
                color = textColor,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(60.dp))

            // Поле телефона
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Номер телефона",
                    color = textColor,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
                )
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    placeholder = { Text("+7 900 000 00 00", color = hintColor) },
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
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Поле пароля
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

            // Ссылки "Забыли пароль" и "Регистрация"
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(
                    onClick = { /* Логика восстановления пароля */ },
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

            // Отправляем кнопку "Далее" в самый низ экрана
            Spacer(modifier = Modifier.weight(1f))

            // Кнопка "Далее"
            Button(
                onClick = { onLoginSuccess() },
                modifier = Modifier
                    .width(140.dp)
                    .height(48.dp)
                    .padding(bottom = 0.dp)
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

            Spacer(modifier = Modifier.height(48.dp)) // Отступ от низа экрана
        }
    }
}