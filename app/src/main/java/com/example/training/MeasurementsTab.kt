package com.example.training

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MeasurementsTab() {
    // 1. Состояние календаря
    val dateFormat = remember { SimpleDateFormat("dd.MM", Locale.getDefault()) }
    var selectedDate by remember { mutableStateOf(dateFormat.format(Date())) }

    // 2. Список названий замеров
    val measurementsList = listOf(
        "Вес",
        "Плечи",
        "Грудь",
        "Талия (расслаб.)",
        "Талия (напряж.)",
        "Живот (расслаб.)",
        "Живот (напряж.)",
        "Ягодицы",
        "Бедро",
        "Голень",
        "Рука (расслаб.)",
        "Рука (напряж.)",
        "Шея"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Ваши замеры", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        // Наш переиспользуемый календарь
        DateCalendar(
            selectedDate = selectedDate,
            onDateSelected = { selectedDate = it }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Данные за $selectedDate",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))

        // 3. Генерация строк с замерами
        measurementsList.forEach { measurement ->
            MeasurementRow(title = measurement)
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

// Компонент для одной строки замера
@Composable
fun MeasurementRow(title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp), // Отступы между строками
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Название замера
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f) // Занимает доступное место слева
        )

        // Серый прямоугольник (заглушка под ввод)
        Box(
            modifier = Modifier
                .width(100.dp)
                .height(40.dp)
                .background(
                    color = Color.LightGray.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(8.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            // Здесь пока пусто. Позже сюда можно добавить TextField для ввода
            // или Text с сохраненным значением из базы данных.
        }
    }
}