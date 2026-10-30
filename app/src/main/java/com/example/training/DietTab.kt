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

// Модель для КБЖУ
data class Macros(
    val calories: Int,
    val proteins: Int,
    val fats: Int,
    val carbs: Int
)

@Composable
fun DietTab() {
    // 1. Состояние календаря
    val dateFormat = remember { SimpleDateFormat("dd.MM", Locale.getDefault()) }
    var selectedDate by remember { mutableStateOf(dateFormat.format(Date())) }

    // 2. Имитация базы данных КБЖУ по дням
    // В реальности это будет приходить с сервера. Сделаем разные значения для примера.
    val dailyMacros = remember {
        val today = dateFormat.format(Date())
        val tomorrow = dateFormat.format(Date(System.currentTimeMillis() + 86400000))
        mapOf(
            today to Macros(2000, 150, 60, 200),
            tomorrow to Macros(1850, 120, 50, 180),
            // Для остальных дней будет дефолтное значение
        )
    }

    // Получаем КБЖУ для выбранного дня. Если нет — берем стандартные.
    val currentMacros = dailyMacros[selectedDate] ?: Macros(2000, 120, 70, 250)

    // 3. Список рецептов
    val recipes = listOf(
        "Овсяноблин с курицей",
        "Салат с тунцом и яйцом",
        "Гречка с говядиной",
        "Творожная запеканка",
        "Куриная грудка с овощами"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Дневник питания", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        // Вставляем наш новый компонент календаря
        DateCalendar(
            selectedDate = selectedDate,
            onDateSelected = { selectedDate = it }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Заголовок "Дневной рацион"
        Text("Дневной рацион", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        // Плашки КБЖУ
        // Используем Row с weight, чтобы 4 плашки влезли в один ряд
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MacroCard(
                modifier = Modifier.weight(1f),
                title = "Калории",
                value = "${currentMacros.calories}",
                unit = "ккал",
                color = MaterialTheme.colorScheme.primaryContainer
            )
            MacroCard(
                modifier = Modifier.weight(1f),
                title = "Белки",
                value = "${currentMacros.proteins}",
                unit = "г",
                color = MaterialTheme.colorScheme.secondaryContainer
            )
            MacroCard(
                modifier = Modifier.weight(1f),
                title = "Жиры",
                value = "${currentMacros.fats}",
                unit = "г",
                color = MaterialTheme.colorScheme.tertiaryContainer
            )
            MacroCard(
                modifier = Modifier.weight(1f),
                title = "Углеводы",
                value = "${currentMacros.carbs}",
                unit = "г",
                color = MaterialTheme.colorScheme.surfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Заголовок "Рецепты"
        Text("Рецепты", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        // Серые прямоугольники с названиями блюд
        recipes.forEach { recipe ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
                    .height(80.dp), // Высота плашки
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.LightGray.copy(alpha = 0.3f) // Серый цвет
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    contentAlignment = Alignment.CenterStart // Текст слева по центру вертикали
                ) {
                    Text(
                        text = recipe,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.DarkGray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

// Компонент для плашки с КБЖУ (чтобы не дублировать код)
@Composable
fun MacroCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    unit: String,
    color: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally // Текст по центру
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = unit,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}