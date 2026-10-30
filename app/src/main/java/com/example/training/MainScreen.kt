package com.example.training

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource

// Модель для нашей навигации
data class BottomNavItem(
    val title: String,
    val iconRes: Int // ID ресурса иконки
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onProfileClick: () -> Unit // <--- НОВЫЙ ПАРАМЕТР
) {
    val navItems = listOf(
        BottomNavItem("Домашняя", R.drawable.ic_home),
        BottomNavItem("Рацион", R.drawable.ic_diet),
        BottomNavItem("Замеры", R.drawable.ic_measurements),
        BottomNavItem("Тренировка", R.drawable.ic_workout)
    )

    var selectedItemIndex by remember { mutableIntStateOf(0) }

    Scaffold(
        // ДОБАВЛЯЕМ HEADER В ВЕРХНЮЮ ЧАСТЬ ЭКРАНА
        topBar = {
            Header(
                userName = "Александр", // Пока захардкодим имя
                onProfileClick = onProfileClick
            )
        },
        bottomBar = {
            NavigationBar {
                navItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = item.iconRes),
                                contentDescription = item.title
                            )
                        },
                        label = { Text(item.title) },
                        selected = selectedItemIndex == index,
                        onClick = { selectedItemIndex = index }
                    )
                }
            }
        }
    ) { innerPadding ->
        androidx.compose.foundation.layout.Box(
            modifier = Modifier.padding(innerPadding)
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