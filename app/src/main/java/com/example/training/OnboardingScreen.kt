package com.example.training

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import androidx.compose.foundation.background
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.unit.dp

// Данные для каждого экрана онбординга
data class OnboardingPage(
    val imageRes: Int,
    val title: String,
    val description: String
)

@Composable
fun OnboardingScreen(onFinish: () -> Unit) {
    val pages = listOf(
        OnboardingPage(
            R.drawable.screen_1_1,
            "TrainMate",
            "Сделает твой прогресс в тренировках стабильным и наглядным!"
        ),
        OnboardingPage(
            R.drawable.screen_2_1,
            "",
            "Отдых и растяжка ускорят восстановление мышц"
        ),
        OnboardingPage(
            R.drawable.screen_3_1,
            "",
            "Отслеживать прогресс в таблицах теперь в разы удобнее"
        ),
        OnboardingPage(
            R.drawable.screen_4_1,
            "",
            "График занятости тренеров облегчит планирование своих тренировок"
        ),
        OnboardingPage(
            R.drawable.screen_5_1,
            "",
            "Квалифицированные тренеры помогут составить программу занятий"
        )
    )

    val pagerState = rememberPagerState(pageCount = { pages.size })
    val coroutineScope = rememberCoroutineScope()

    // Основной контейнер, накладывающий слои
    Box(modifier = Modifier.fillMaxSize()) {

        // СЛОЙ 1: Картинки (Pager)
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            Image(
                painter = painterResource(id = pages[page].imageRes),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop // Заполняет весь экран
            )
        }

        // СЛОЙ 2: Затемнение снизу (чтобы белый текст читался)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable(
                    interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                    indication = null
                ) {
                    if (pagerState.currentPage < pages.size - 1) {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                }
        )

        // СЛОЙ 3: Текст и кнопки
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Bottom // Прижимаем всё к низу
        ) {
            // Текст текущей страницы
            val currentPage = pages[pagerState.currentPage]

            Text(
                text = currentPage.title,
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = currentPage.description,
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 18.sp,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Индикатор (Slide Bar) со стрелочкой
            SlideBarIndicator(
                totalDots = pages.size,
                currentPage = pagerState.currentPage
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Кнопки
            if (pagerState.currentPage == 0) {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(1)
                        }
                    },

                    modifier = Modifier
                        .fillMaxWidth() // Растянуть на всю ширину
                        .height(56.dp) // Высота кнопки
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFFFFAE25), // Начальный цвет
                                    Color(0xFFFF6417)  // Конечный цвет
                                )
                            ),
                            shape = RoundedCornerShape(10.dp),
                        ),

                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.Black
                    ),

                    shape = RoundedCornerShape(10.dp), // Скругленные углы

                ) {
                    Text(
                        text = "Подробнее",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else if (pagerState.currentPage == pages.size - 1) {
                Button(
                    onClick = onFinish,

                    modifier = Modifier
                        .fillMaxWidth() // Растянуть на всю ширину
                        .height(56.dp) // Высота кнопки
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFFFFAE25), // Начальный цвет
                                    Color(0xFFFF6417)  // Конечный цвет
                                )
                            ),
                            shape = RoundedCornerShape(10.dp),
                        ),

                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.Black
                    ),

                    shape = RoundedCornerShape(10.dp), // Скругленные углы

                ) {
                    Text(
                        text = "Продолжить",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                Spacer(modifier = Modifier.height(56.dp))
            }
        }

        if (pagerState.currentPage in 1..3) {
            OutlinedButton(
                onClick = onFinish,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 48.dp, end = 24.dp),
                border = BorderStroke(1.dp, Color(0xFFC7C7C7)), // Серый контур
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color(0xFFC7C7C7) // Серый текст
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text("Пропустить", fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun SlideBarIndicator(
    totalDots: Int,
    currentPage: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 0 until totalDots) {
            if (i == currentPage) {
                // Активная точка (со стрелкой)
                Row(
                    modifier = Modifier
                        .width(48.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color.White),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        tint = Color.Black, // Стрелка черная на белом фоне
                        modifier = Modifier.size(16.dp)
                    )
                }
            } else {
                // Неактивная точка
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.5f))
                )
            }

            if (i < totalDots - 1) {
                Spacer(modifier = Modifier.width(4.dp))
            }
        }
    }
}