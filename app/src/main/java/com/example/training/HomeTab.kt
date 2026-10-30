package com.example.training

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Bedtime
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTab() {
    // --- ЦВЕТА ИЗ ДИЗАЙНА ---
    val bgColor = Color(0xFF14110E) // Темный фон
    val textColor = Color.White
    val accentOrange = Color(0xFFFF8C00)
    val darkCardColor = Color(0xFF23201D) // Цвет плашек активности

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

    // --- КАТЕГОРИИ ---
    val categories = listOf("Зарядка", "Кардио", "Силовая")
    var selectedCategory by remember { mutableStateOf("Зарядка") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // 1. КАЛЕНДАРЬ (Просто текст с подчеркиванием)
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(dateList) { date ->
                val isSelected = date == selectedDate
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { selectedDate = date }
                ) {
                    Text(
                        text = date,
                        color = if (isSelected) accentOrange else textColor.copy(alpha = 0.7f),
                        fontSize = 16.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    // Оранжевая линия под выбранной датой
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

        // 2. БАННЕР (Градиентный)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .clip(RoundedCornerShape(40.dp))
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(Color(0xFFFF8C00), Color(0xFFFFD600)) // Оранжевый -> Желтый
                    )
                )
                .padding(horizontal = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Зал", color = Color.Black, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text("18:00", color = Color.Black, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 3. КАТЕГОРИИ (Чипсы)
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(categories) { category ->
                val isSelected = category == selectedCategory
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .border(
                            width = 1.dp,
                            color = if (isSelected) accentOrange else Color.Gray,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .clickable { selectedCategory = category }
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = category,
                        color = if (isSelected) accentOrange else textColor
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 4. КАРТОЧКИ УПРАЖНЕНИЙ (Горизонтальный скролл)
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(3) { index -> // Показываем 3 карточки для примера
                ExerciseCard(
                    // Замени R.drawable.ic_launcher_background на свою картинку упражнения
                    imageRes = R.drawable.ic_launcher_background,
                    title = "Наклоны тренера",
                    time = "15 мин",
                    duration = "3 подхода",
                    modifier = Modifier.width(160.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // 5. ВАША АКТИВНОСТЬ
        Text(
            text = "Ваша активность",
            color = textColor,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        ActivityRow(
            icon = Icons.Default.DirectionsWalk,
            title = "Прогулка",
            value = "7200",
            unit = "/шагов",
            iconColor = Color(0xFFFF8C00),
            bgColor = darkCardColor,
            textColor = textColor
        )
        Spacer(modifier = Modifier.height(12.dp))

        ActivityRow(
            icon = Icons.Default.FitnessCenter,
            title = "Тренировка",
            value = "1,2",
            unit = "/ч",
            iconColor = Color(0xFFFFB300), // Чуть другой оттенок
            bgColor = darkCardColor,
            textColor = textColor
        )
        Spacer(modifier = Modifier.height(12.dp))

        ActivityRow(
            icon = Icons.Default.Restaurant,
            title = "Еда",
            value = "2000",
            unit = "/ккал",
            iconColor = Color(0xFFFF8C00),
            bgColor = darkCardColor,
            textColor = textColor
        )
        Spacer(modifier = Modifier.height(12.dp))

        ActivityRow(
            icon = Icons.Default.Bedtime,
            title = "Сон",
            value = "8,4",
            unit = "/ч",
            iconColor = Color(0xFFFF8C00),
            bgColor = darkCardColor,
            textColor = textColor
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

// КОМПОНЕНТ: КАРТОЧКА УПРАЖНЕНИЯ
@Composable
fun ExerciseCard(
    imageRes: Int,
    title: String,
    time: String,
    duration: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(180.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Фоновая картинка
            androidx.compose.foundation.Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            // Затемнение снизу для читаемости текста
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)),
                            startY = 200f
                        )
                    )
            )
            // Текст поверх картинки
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
            ) {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Bedtime, // Заглушка для часов, можешь заменить на Icons.Default.AccessTime
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$time | $duration",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}

// КОМПОНЕНТ: СТРОКА АКТИВНОСТИ
@Composable
fun ActivityRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String,
    unit: String,
    iconColor: Color,
    bgColor: Color,
    textColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(bgColor)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Круглая иконка с обводкой
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .border(2.dp, iconColor, CircleShape)
                    .background(iconColor.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = title, color = textColor, fontSize = 16.sp, fontWeight = FontWeight.Medium)
        }

        Row(verticalAlignment = Alignment.Bottom) {
            Text(text = value, color = textColor, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(text = unit, color = textColor.copy(alpha = 0.6f), fontSize = 12.sp, modifier = Modifier.padding(bottom = 2.dp))
        }
    }
}