package com.example.training

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Header(
    userName: String,
    onProfileClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp), // Небольшой отступ сверху и снизу
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Круглая "аватарка" (пока серый круг)
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape) // Делаем форму круга
                .background(Color.Gray) // Серый фон
                .clickable { onProfileClick() }, // Обработка клика
            contentAlignment = Alignment.Center
        ) {
            // Внутри круга первая буква имени
            Text(
                text = userName.firstOrNull()?.uppercase() ?: "?", // Берем первую букву
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Имя пользователя
        Text(
            text = userName,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}