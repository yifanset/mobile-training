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
    val bgColor = Color(0xFF14110E)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            // 1. ПРОЗРАЧНОСТЬ: Убрали .background(...) (если он был) или задали Color.Transparent
            .background(bgColor)
            // 2. ОТСТУПЫ:
            // padding(horizontal = 16.dp) — отступы по бокам, чтобы аватарка не прилипала к краю
            // padding(vertical = 12.dp) — отступы сверху и снизу
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Круглая "аватарка" (пока серый круг)
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                // Сделал фон аватарки чуть темнее (Color.DarkGray), чтобы он не сливался с общим фоном приложения
                // Если хочешь оставить ярко-серый, верни Color.Gray
                .background(Color.DarkGray)
                .clickable { onProfileClick() },
            contentAlignment = Alignment.Center
        ) {
            // Внутри круга первая буква имени
            Text(
                text = userName.firstOrNull()?.uppercase() ?: "?",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Имя пользователя
        Text(
            text = userName,
            color = Color.White, // Добавил белый цвет для текста, чтобы он был виден на темной теме
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}