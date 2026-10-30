package com.example.training

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Модель для дня тренировок
data class WorkoutDay(
    val title: String, // Например, "Вторник 18.09"
    val times: List<String> // Список доступного времени
)

@Composable
fun WorkoutTab() {
    // Данные для расписания (в реальности будут приходить из базы)
    val schedule = listOf(
        WorkoutDay("Вторник 18.09", listOf("16:00", "17:00", "18:00", "20:00")),
        WorkoutDay("Среда 19.09", listOf("16:00", "17:00", "20:00", "21:00")),
        WorkoutDay("Четверг 20.09", listOf("16:00", "17:00"))
    )

    // Состояние для выбранного времени (по умолчанию ничего не выбрано)
    var selectedTime by remember { mutableStateOf<String?>(null) }

    // Цвета (подбираем близкие к твоему скриншоту)
    val selectedColor = Color(0xFFFF6B00) // Ярко-оранжевый
    val unselectedColor = Color(0xFF4A3A30) // Темно-коричневый
    val textColor = Color.White

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E1714)) // Темный фон на весь экран
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Заголовок "Запись на тренировку"
        Text(
            text = "Запись на тренировку",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = textColor
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Селектор недели (с обводкой)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(Color.Transparent, RoundedCornerShape(28.dp))
                .then(
                    Modifier.padding(1.dp) // Внутренний отступ для эффекта обводки
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Используем OutlinedButton для создания рамки
            OutlinedButton(
                onClick = { /* Логика переключения недели назад */ },
                shape = RoundedCornerShape(28.dp),
                border = BorderStroke(1.dp, Color.Gray),
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.size(48.dp)
            ) {
                Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Назад", tint = textColor)
            }

            Text(
                text = "14-20 сентября",
                color = textColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )

            OutlinedButton(
                onClick = { /* Логика переключения недели вперед */ },
                shape = RoundedCornerShape(28.dp),
                border = BorderStroke(1.dp, Color.Gray),
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.size(48.dp)
            ) {
                Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Вперед", tint = textColor)
            }
        }

        // Делаем вид, что селектор это единая плашка (обводка вокруг всего)
        // В реальности код выше делает кнопки отдельно, но для визуального сходства
        // можно использовать просто Row с border, но давай оставим функциональные кнопки.

        Spacer(modifier = Modifier.height(32.dp))

        // Перебираем дни и выводим их
        schedule.forEach { day ->
            Column(modifier = Modifier.fillMaxWidth()) {
                // Заголовок дня
                Text(
                    text = day.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Горизонтальная линия-разделитель
                Divider(color = Color.Gray.copy(alpha = 0.5f), thickness = 1.dp)

                Spacer(modifier = Modifier.height(12.dp))

                // Время (горизонтальный скролл, если не влезет)
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(day.times) { time ->
                        val isSelected = selectedTime == time

                        // Используем FilterChip, но кастомизируем цвета
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                // Если нажали на выбранное - отменяем выбор, иначе выбираем
                                selectedTime = if (isSelected) null else time
                            },
                            label = {
                                Text(
                                    text = time,
                                    color = textColor,
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                )
                            },
                            shape = RoundedCornerShape(20.dp), // Форма "таблетки"
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = unselectedColor, // Темный фон
                                selectedContainerColor = selectedColor, // Оранжевый при выборе
                            ),
                            border = null // Убираем рамку, если она есть по умолчанию
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}