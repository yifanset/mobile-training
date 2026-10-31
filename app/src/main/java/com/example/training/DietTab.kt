package com.example.training

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DietTab() {
    // --- ЦВЕТА ИЗ ДИЗАЙНА ---
    val bgColor = Color(0xFF14110E)
    val textColor = Color.White
    val accentOrange = Color(0xFFFF8C00)
    val cardBgColor = Color(0xFF23201D)

    // --- КАЛЕНДАРЬ ---
    val dateList = remember {
        val dates = mutableListOf<String>()
        val calendar = Calendar.getInstance()
        val format = SimpleDateFormat("dd.MM", Locale.getDefault())
        for (i in 0 until 6) {
            dates.add(format.format(calendar.time))
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }
        dates
    }
    var selectedDate by remember { mutableStateOf(dateList[0]) }

    // --- ДАННЫЕ ДЛЯ ПЛАШЕК ---
    val macros = listOf(
        MacroData("Калории", "Введите данные", "ккал"),
        MacroData("Жиры", "4300", "г"),
        MacroData("Протеин", "4300", "г"),
        MacroData("Углеводы", "4300", "г")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // 1. КАЛЕНДАРЬ
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(dateList) { date ->
                val isSelected = date == selectedDate
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { selectedDate = date } // не забудь import androidx.compose.foundation.clickable
                ) {
                    Text(
                        text = date,
                        color = if (isSelected) accentOrange else textColor.copy(alpha = 0.7f),
                        fontSize = 16.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .width(40.dp)
                            .height(2.dp)
                            .background(if (isSelected) accentOrange else Color.Transparent)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 2. ДНЕВНОЙ РАЦИОН
        Text(
            text = "Дневной рацион",
            color = textColor,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Плашки КБЖУ (горизонтальный скролл, чтобы не сжимались)
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(macros) { macro ->
                MacroCard(
                    title = macro.title,
                    value = macro.value,
                    unit = macro.unit,
                    bgColor = cardBgColor,
                    textColor = textColor
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // 3. РЕЦЕПТЫ
        Text(
            text = "Рецепты",
            color = textColor,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Карточки рецептов
        // (Замени R.drawable.ic_launcher_background на свои картинки еды)
        RecipeCard(
            title = "Гречка с молоком",
            time = "20 мин",
            imageRes = R.drawable.ic_launcher_background
        )
        Spacer(modifier = Modifier.height(16.dp))
        RecipeCard(
            title = "Салат с лососем",
            time = "15 мин",
            imageRes = R.drawable.ic_launcher_background
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

// Модель для КБЖУ
data class MacroData(val title: String, val value: String, val unit: String)

// Компонент плашки КБЖУ
@Composable
fun MacroCard(
    title: String,
    value: String,
    unit: String,
    bgColor: Color,
    textColor: Color
) {
    Card(
        modifier = Modifier
            .width(110.dp)
            .height(140.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Белый кружок сверху
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            )

            Column {
                Text(
                    text = title,
                    color = textColor.copy(alpha = 0.6f),
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = value,
                        color = textColor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    if (value != "Введите данные") {
                        Text(
                            text = unit,
                            color = textColor.copy(alpha = 0.6f),
                            fontSize = 10.sp,
                            modifier = Modifier.padding(bottom = 2.dp, start = 2.dp)
                        )
                    }
                }
            }
        }
    }
}

// Компонент карточки рецепта
@Composable
fun RecipeCard(
    title: String,
    time: String,
    imageRes: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Картинка на фоне
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Градиент снизу для читаемости текста
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)),
                            startY = 300f
                        )
                    )
            )

            // Текст поверх картинки
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.8f),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = time,
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}