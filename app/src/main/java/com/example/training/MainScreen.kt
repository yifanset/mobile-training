package com.example.training

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp

data class BottomNavItem(
    val title: String,
    val iconRes: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onProfileClick: () -> Unit
) {
    val navItems = listOf(
        BottomNavItem("Домашняя", R.drawable.ic_home),
        BottomNavItem("Рацион", R.drawable.ic_diet),
        BottomNavItem("Замеры", R.drawable.ic_measurements),
        BottomNavItem("Тренировка", R.drawable.ic_workout)
    )

    var selectedItemIndex by remember { mutableIntStateOf(0) }

    // Цвета
    val bgColor = Color(0xFF14110E) // Темный фон приложения
    val panelColor = Color(0xFF23201D) // Цвет меню (чуть светлее фона)

    Scaffold(
        containerColor = bgColor,
        topBar = {
            Header(
                userName = "Александр",
                onProfileClick = onProfileClick
            )
        },
        bottomBar = {
            // Кастомная нижняя панель
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    // Скругление только верхних углов
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    // Легкая тень для отделения от контента
                    .shadow(elevation = 8.dp, shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
                color = panelColor
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .padding(horizontal = 8.dp), // Небольшой отступ по бокам внутри панели
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    navItems.forEachIndexed { index, item ->
                        val isSelected = selectedItemIndex == index

                        // Зона клика
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clickable { selectedItemIndex = index },
                            contentAlignment = Alignment.Center
                        ) {
                            // Сама иконка
                            Icon(
                                imageVector = ImageVector.vectorResource(id = item.iconRes),
                                contentDescription = item.title,
                                modifier = Modifier
                                    .size(28.dp) // Фиксированный размер (не увеличиваем)
                                    // БЕЛОЕ СВЕЧЕНИЕ: если выбрано — добавляем тень, если нет — без тени
                                    .then(
                                        if (isSelected) {
                                            Modifier.shadow(
                                                elevation = 12.dp,
                                                shape = CircleShape,
                                                ambientColor = Color.White,
                                                spotColor = Color.White
                                            )
                                        } else Modifier
                                    ),
                                // Цвет иконки: белый, если выбрана, иначе полупрозрачный белый
                                tint = if (isSelected) Color.White else Color.White.copy(alpha = 0.4f)
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            when (selectedItemIndex) {
                0 -> HomeTab()
                1 -> DietTab()
                2 -> MeasurementsTab()
                3 -> WorkoutTab()
            }
        }
    }
}