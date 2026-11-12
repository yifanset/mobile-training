package com.example.training

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*

// Модель для дня
data class WorkoutDayData(
    val dateString: String, // "18.09"
    val dayOfWeek: String,  // "Пн"
    val fullDate: Date
)

@Composable
fun WorkoutTab() {
    // --- ЦВЕТА ИЗ ДИЗАЙНА ---
    val bgColor = Color(0xFF14110E)
    val textColor = Color.White
    val accentOrange = Color(0xFFFF8C00)
    val darkCardColor = Color(0xFF23201D)

    // --- ЛОГИКА НЕДЕЛИ ---
    // Индекс смещения недели (0 - текущая, -1 - прошлая, 1 - следующая)
    var weekOffset by remember { mutableIntStateOf(0) }

    // Список выбранных дат (храним строки типа "18.09 17:00")
    var selectedSlots by remember { mutableStateOf(setOf<String>()) }

    val daysOfWeek = remember { listOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс") }
    val availableTimes = listOf("16:00", "17:00", "18:00", "19:00")

    // Функция генерации 7 дней для выбранной недели
    fun getDaysForWeek(offset: Int): List<WorkoutDayData> {
        val list = mutableListOf<WorkoutDayData>()
        val calendar = Calendar.getInstance()

        // Находим понедельник текущей недели
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        if (calendar.time.after(Date())) {
            calendar.add(Calendar.WEEK_OF_YEAR, -1)
        }

        // Добавляем смещение недель
        calendar.add(Calendar.WEEK_OF_YEAR, offset)

        val format = SimpleDateFormat("dd.MM", Locale.getDefault())
        val dayFormat = SimpleDateFormat("EE", Locale("ru")) // "Пн", "Вт"

        for (i in 0 until 7) {
            list.add(
                WorkoutDayData(
                    dateString = format.format(calendar.time),
                    dayOfWeek = dayFormat.format(calendar.time).replaceFirstChar { it.uppercase() }.substring(0, 2),
                    fullDate = calendar.time
                )
            )
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }
        return list
    }

    val currentWeekDays = remember(weekOffset) { getDaysForWeek(weekOffset) }

    // Форматирование заголовка недели (например, "14-20 сентября")
    val weekTitle = remember(weekOffset) {
        val firstDay = currentWeekDays.first().fullDate
        val lastDay = currentWeekDays.last().fullDate
        val monthFormat = SimpleDateFormat("LLLL", Locale("ru"))

        val startDayStr = SimpleDateFormat("d", Locale.getDefault()).format(firstDay)
        val endDayStr = SimpleDateFormat("d", Locale.getDefault()).format(lastDay)
        val monthStr = monthFormat.format(lastDay)

        "$startDayStr-$endDayStr $monthStr"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // ЗАГОЛОВОК
        Text(
            text = "Запись на тренировку",
            color = textColor,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        // СЕЛЕКТОР НЕДЕЛИ
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(28.dp))
                .border(BorderStroke(1.dp, Color.Gray), RoundedCornerShape(28.dp)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { weekOffset-- }) {
                Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Назад", tint = textColor)
            }

            Text(
                text = weekTitle,
                color = textColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )

            IconButton(onClick = { weekOffset++ }) {
                Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Вперед", tint = textColor)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // СПИСОК ДНЕЙ
        currentWeekDays.forEach { day ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                // Заголовок дня
                Text(
                    text = "${day.dayOfWeek} ${day.dateString}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )

                // Линия
                Divider(color = Color.Gray.copy(alpha = 0.3f), thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))

                // Время
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(availableTimes) { time ->
                        // Уникальный ключ для каждого слота (Дата + Время)
                        val slotKey = "${day.dateString} $time"
                        val isSelected = selectedSlots.contains(slotKey)

                        // Кнопка времени
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (isSelected) accentOrange else darkCardColor)
                                .clickable {
                                    // Логика добавления/удаления из списка
                                    selectedSlots = if (isSelected) {
                                        selectedSlots - slotKey
                                    } else {
                                        selectedSlots + slotKey
                                    }
                                }
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = time,
                                color = if (isSelected) Color.Black else textColor,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // КНОПКА "ЗАПИСАТЬСЯ" (появляется, если выбрано >= 1)
        if (selectedSlots.isNotEmpty()) {
            Button(
                onClick = {
                    // Заглушка
                    println("Запись на слоты: $selectedSlots")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFFFFAE25), Color(0xFFFF6417))
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Записаться (${selectedSlots.size})",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}