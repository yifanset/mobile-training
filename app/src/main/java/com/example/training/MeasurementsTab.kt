package com.example.training

import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MeasurementsTab() {
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

    // --- СПИСОК ЗАМЕРОВ ---
    // (Добавил пару для симметрии сетки, чтобы было 6 штук)
    val measurements = listOf(
        MeasurementData("Талия расслаб.", "65,5", "см", "+1", true),
        MeasurementData("Бедро", "Введите замер", "", "0", false),
        MeasurementData("Голень", "55,1", "см", "+1", true),
        MeasurementData("Талия расслаб.", "65,5", "см", "+1", true),
        MeasurementData("Бедро", "Введите замер", "", "0", false),
        MeasurementData("Голень", "55,1", "см", "+1", true)
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
                    modifier = Modifier.clickable { selectedDate = date }
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

        // 2. ЗАГОЛОВОК
        Text(
            text = "Замеры",
            color = textColor,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        // СЕТКА ЗАМЕРОВ (2 ряда по 3 колонки)
        // Используем Column и Row для жесткой сетки
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            for (row in 0 until 2) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    for (col in 0 until 3) {
                        val index = row * 3 + col
                        if (index < measurements.size) {
                            MeasurementCard(
                                modifier = Modifier.weight(1f),
                                data = measurements[index],
                                bgColor = cardBgColor,
                                textColor = textColor
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // 3. МОЙ ПРОГРЕСС
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Мой прогресс",
                color = textColor,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            // Выпадающий список (Пока просто визуальная кнопка)
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(accentOrange)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Вес", color = Color.Black, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "71,3 КГ",
            color = accentOrange,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 4. ГРАФИК (Canvas)
        WeightChart(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

// Модель данных для карточки замера
data class MeasurementData(
    val title: String,
    val value: String,
    val unit: String,
    val diff: String,
    val isPositive: Boolean
)

// КОМПОНЕНТ: КАРТОЧКА ЗАМЕРА
@Composable
fun MeasurementCard(
    modifier: Modifier = Modifier,
    data: MeasurementData,
    bgColor: Color,
    textColor: Color
) {
    Card(
        modifier = modifier.height(120.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Белый кружок
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            )

            Column {
                Text(
                    text = data.title,
                    color = textColor.copy(alpha = 0.6f),
                    fontSize = 10.sp,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = data.value,
                        color = textColor,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    if (data.value != "Введите замер") {
                        Text(
                            text = data.unit,
                            color = textColor.copy(alpha = 0.6f),
                            fontSize = 10.sp,
                            modifier = Modifier.padding(bottom = 2.dp, start = 2.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                // Разница (зеленая или красная)
                if (data.value != "Введите замер") {
                    Text(
                        text = if (data.isPositive) "↘ ${data.diff}" else "↗ ${data.diff}",
                        color = if (data.isPositive) Color(0xFF4CAF50) else Color.Red,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    Text(
                        text = data.diff,
                        color = textColor.copy(alpha = 0.6f),
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

// КОМПОНЕНТ: ГРАФИК
@Composable
fun WeightChart(modifier: Modifier = Modifier) {
    val textColor = Color.White.copy(alpha = 0.5f)
    val lineColor = Color(0xFFFF8C00)

    // Данные графика (для примера)
    val weights = listOf(75f, 76f, 74f, 73f, 73f, 74f, 71.3f)
    val labels = listOf("пн", "вт", "ср", "чт", "пт", "сб", "вс")

    // Границы оси Y
    val minWeight = 60f
    val maxWeight = 90f

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val leftPadding = 80f // Отступ слева под текст оси Y
        val bottomPadding = 40f // Отступ снизу под дни недели

        val chartWidth = width - leftPadding
        val chartHeight = height - bottomPadding

        // 1. Рисуем горизонтальные линии и подписи оси Y
        val ySteps = 4 // 60, 70, 80, 90
        for (i in 0..ySteps) {
            val y = chartHeight - (chartHeight / ySteps * i)
            val weightLabel = (minWeight + (maxWeight - minWeight) / ySteps * i).toInt()

            // Линия
            drawLine(
                color = Color.DarkGray.copy(alpha = 0.5f),
                start = Offset(leftPadding, y),
                end = Offset(width, y),
                strokeWidth = 2f
            )

            // Текст оси Y (рисуем вручную, так как Canvas не умеет в текст из коробки)
            // В реальном проекте лучше использовать drawText с TextMeasurer, но для простоты
            // можно сделать подписи через Row/Column вокруг Canvas.
            // Здесь для примера просто оставим линии.
        }

        // 2. Рисуем линию графика
        val path = Path()
        val points = mutableListOf<Offset>()

        weights.forEachIndexed { index, weight ->
            // Вычисляем X (равномерно распределяем по ширине)
            val x = leftPadding + (chartWidth / (weights.size - 1)) * index

            // Вычисляем Y (инвертируем, так как в Canvas 0 - это верх)
            val normalizedWeight = (weight - minWeight) / (maxWeight - minWeight)
            val y = chartHeight - (normalizedWeight * chartHeight)

            points.add(Offset(x, y))
        }

        // Строим путь
        points.forEachIndexed { index, point ->
            if (index == 0) {
                path.moveTo(point.x, point.y)
            } else {
                path.lineTo(point.x, point.y)
            }
        }

        // Рисуем линию
        drawPath(
            path = path,
            color = lineColor,
            style = Stroke(width = 4f)
        )

        // 3. Рисуем точки на графике
        points.forEach { point ->
            drawCircle(
                color = lineColor,
                radius = 6f,
                center = point
            )
        }
    }
}