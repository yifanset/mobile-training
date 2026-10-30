package com.example.training

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
fun RegisterScreen(
    onBackClick: () -> Unit,
    onLoginClick: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    // Состояния для всех полей
    var lastName by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") } // Новое поле
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var errorText by remember { mutableStateOf<String?>(null) }

    // Цвета
    val textColor = Color.White
    val borderColor = Color.White
    val hintColor = Color(0xFF888888)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF1A1512), Color(0xFF0F0D0B)) // Темный фон
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()), // Включаем прокрутку
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            // Заголовок
            Text(
                text = "Регистрация",
                color = textColor,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(40.dp))

            // ФАМИЛИЯ
            RegisterTextField(
                label = "Фамилия",
                value = lastName,
                onValueChange = { lastName = it },
                placeholder = "Иванов",
                textColor = textColor,
                borderColor = borderColor,
                hintColor = hintColor
            )

            // ИМЯ
            RegisterTextField(
                label = "Имя",
                value = firstName,
                onValueChange = { firstName = it },
                placeholder = "Иван",
                textColor = textColor,
                borderColor = borderColor,
                hintColor = hintColor
            )

            // ТЕЛЕФОН
            RegisterTextField(
                label = "Номер телефона",
                value = phone,
                onValueChange = { phone = it },
                placeholder = "+7 900 000 00 00",
                textColor = textColor,
                borderColor = borderColor,
                hintColor = hintColor,
                keyboardType = KeyboardType.Phone
            )

            // EMAIL (Добавлен по твоей просьбе)
            RegisterTextField(
                label = "Email",
                value = email,
                onValueChange = { email = it },
                placeholder = "example@mail.ru",
                textColor = textColor,
                borderColor = borderColor,
                hintColor = hintColor,
                keyboardType = KeyboardType.Email
            )

            // ПАРОЛЬ
            RegisterTextField(
                label = "Пароль",
                value = password,
                onValueChange = { password = it },
                placeholder = "Пароль",
                textColor = textColor,
                borderColor = borderColor,
                hintColor = hintColor,
                isPassword = true
            )

            // ПОВТОРИТЕ ПАРОЛЬ
            RegisterTextField(
                label = "Повторите пароль",
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                placeholder = "Повторите пароль",
                textColor = textColor,
                borderColor = borderColor,
                hintColor = hintColor,
                isPassword = true,
                isError = errorText != null
            )

            // Ошибка (если пароли не совпадают)
            if (errorText != null) {
                Text(
                    text = errorText!!,
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, start = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Кнопка "Далее"
            Button(
                onClick = {
                    if (password != confirmPassword) {
                        errorText = "Пароли не совпадают!"
                    } else if (password.isEmpty() || phone.isEmpty()) {
                        errorText = "Заполните обязательные поля"
                    } else {
                        errorText = null
                        onRegisterSuccess()
                    }
                },
                modifier = Modifier
                    .width(140.dp)
                    .height(48.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFFB3BFFF), Color(0xFF5A75FF)) // Сине-голубой градиент как на фото
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

            Spacer(modifier = Modifier.height(32.dp))

            // Ссылка "Уже есть аккаунт?" (для удобства)
            TextButton(onClick = onLoginClick) {
                Text("Уже есть аккаунт? Войти", color = Color(0xFF7AA8FF), fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

// Вспомогательный компонент, чтобы не дублировать код для каждого поля
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    textColor: Color,
    borderColor: Color,
    hintColor: Color,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    isError: Boolean = false
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp) // Отступ между полями
    ) {
        Text(
            text = label,
            color = textColor,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = hintColor) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            isError = isError,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = borderColor,
                unfocusedBorderColor = borderColor,
                errorBorderColor = Color.Red,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedTextColor = textColor,
                unfocusedTextColor = textColor,
                cursorColor = textColor
            ),
            visualTransformation = if (isPassword) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            singleLine = true
        )
    }
}