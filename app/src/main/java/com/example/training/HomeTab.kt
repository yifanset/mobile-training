package com.example.training

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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

// Модель упражнения
data class ExerciseData(val title: String, val time: String, val duration: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTab() {
    // --- ЦВЕТА ИЗ ДИЗАЙНА ---
    val bgColor = Color(0xFF14110E)
    val textColor = Color.White
    val accentOrange = Color(0xFFFF8C00)
    val darkCardColor = Color(0xFF23201D)

    // --- БЕСКОНЕЧНЫЙ КАЛЕНДАРЬ ---
    val totalDays = 10000 // 10000 дней в обе стороны
    val centerIndex = totalDays / 2
    val dateFormat = remember { SimpleDateFormat("dd.MM", Locale.getDefault()) }

    // Состояние скролла для календаря
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = centerIndex - 2)

    // Выбранная дата (по умолчанию сегодня)
    var selectedDateString by remember { mutableStateOf(dateFormat.format(Date())) }

    // Функция получения даты по индексу (0 - это 5000 дней назад, 5000 - сегодня)
    fun getDateForIndex(index: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, index - centerIndex)
        return calendar.time
    }

    // --- ГЕНЕРАЦИЯ СЛУЧАЙНЫХ ТРЕНИРОВОК ---
    // Храним расписание в Map: Ключ - дата (строка), Значение - текст баннера
    val schedule = remember {
        val map = mutableMapOf<String, String>()
        val places = listOf("Зал", "Бассейн", "Кроссфит", "Йога", "Растяжка")
        val times = listOf("10:00", "12:00", "18:00", "19:30", "20:00")
        val random = Random()

        // Генерируем "случайные" тренировки для 200 дней вперед и назад
        for (i in -100..100) {
            val cal = Calendar.getInstance()
            cal.add(Calendar.DAY_OF_YEAR, i)
            val dateStr = dateFormat.format(cal.time)
            // У каждого дня есть 70% шанс, что там будет тренировка
            if (random.nextInt(100) < 70) {
                map[dateStr] = "${places.random()} ${times.random()}"
            } else {
                map[dateStr] = "Отдых"
            }
        }
        map
    }

    // Получаем текст для баннера. Если даты нет в базе (мы ушли далеко), показываем "Нет данных"
    val bannerText = schedule[selectedDateString] ?: "Нет тренировок"

    // --- КАТЕГОРИИ И УПРАЖНЕНИЯ ---
    val categories = listOf("Зарядка", "Кардио", "Силовая")
    var selectedCategory by remember { mutableStateOf("Зарядка") }

    val exercisesByCategory = mapOf(
        "Зарядка" to listOf(
            ExerciseData("Наклоны головы", "5 мин", "10 повторений"),
            ExerciseData("Вращения плечами", "5 мин", "15 повторений"),
            ExerciseData("Наклоны корпуса", "10 мин", "10 повторений")
        ),
        "Кардио" to listOf(
            ExerciseData("Бег на месте", "15 мин", "2 подхода"),
            ExerciseData("Прыжки на скакалке", "10 мин", "3 подхода"),
            ExerciseData("Берпи", "15 мин", "4 подхода")
        ),
        "Силовая" to listOf(
            ExerciseData("Приседания с весом", "15 мин", "3 подхода"),
            ExerciseData("Отжимания от пола", "15 мин", "3 подхода"),
            ExerciseData("Планка", "10 мин", "2 подхода")
        )
    )

    val currentExercises = exercisesByCategory[selectedCategory] ?: emptyList()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // 1. БЕСКОНЕЧНЫЙ КАЛЕНДАРЬ
        LazyRow(
            state = listState,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(totalDays) { index ->
                val date = getDateForIndex(index)
                val dateString = dateFormat.format(date)
                val isSelected = dateString == selectedDateString

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { selectedDateString = dateString }
                ) {
                    Text(
                        text = dateString,
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

        // 2. БАННЕР
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .clip(RoundedCornerShape(40.dp))
                .background(
                    Brush.horizontalGradient(
                        // Если тренировка есть - оранжевый, если нет - серый
                        colors = if (bannerText != "Отдых" && bannerText != "Нет тренировок")
                            listOf(Color(0xFFFF8C00), Color(0xFFFFD600))
                        else listOf(Color(0xFF333333), Color(0xFF555555))
                    )
                )
                .padding(horizontal = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = bannerText,
                color = if (bannerText != "Отдых" && bannerText != "Нет тренировок") Color.Black else Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 3. КАТЕГОРИИ
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

        // 4. КАРТОЧКИ УПРАЖНЕНИЙ (Зависят от выбранной категории)
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(currentExercises) { exercise ->
                ExerciseCard(
                    imageRes = R.drawable.ic_launcher_background, // Замени на свои картинки
                    title = exercise.title,
                    time = exercise.time,
                    duration = exercise.duration,
                    modifier = Modifier.width(160.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // 5. ВАША АКТИВНОСТЬ (Вместо цифр X)
        Text(
            text = "Ваша активность",
            color = textColor,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        ActivityRow(icon = Icons.Default.DirectionsWalk, title = "Прогулка", value = "X", unit = "/шагов", iconColor = accentOrange, bgColor = darkCardColor, textColor = textColor)
        Spacer(modifier = Modifier.height(12.dp))
        ActivityRow(icon = Icons.Default.FitnessCenter, title = "Тренировка", value = "X", unit = "/ч", iconColor = Color(0xFFFFB300), bgColor = darkCardColor, textColor = textColor)
        Spacer(modifier = Modifier.height(12.dp))
        ActivityRow(icon = Icons.Default.Restaurant, title = "Еда", value = "X", unit = "/ккал", iconColor = accentOrange, bgColor = darkCardColor, textColor = textColor)
        Spacer(modifier = Modifier.height(12.dp))
        ActivityRow(icon = Icons.Default.Bedtime, title = "Сон", value = "X", unit = "/ч", iconColor = accentOrange, bgColor = darkCardColor, textColor = textColor)

        Spacer(modifier = Modifier.height(24.dp))
    }
}

// КОМПОНЕНТ: КАРТОЧКА УПРАЖНЕНИЯ (Без изменений)
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
            androidx.compose.foundation.Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
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
                        imageVector = Icons.Default.AccessTime, // Поменял на часы
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

// КОМПОНЕНТ: СТРОКА АКТИВНОСТИ (Без изменений)
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