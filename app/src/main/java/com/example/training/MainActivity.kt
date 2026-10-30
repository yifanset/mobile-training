package com.example.training

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.training.ui.theme.TrainingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TrainingTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "onboarding") {

        composable("onboarding") {
            OnboardingScreen(
                onFinish = {
                    navController.navigate("login") {
                        popUpTo("onboarding") { inclusive = true }
                    }
                }
            )
        }

        // Экран ВХОДА
        composable("login") {
            LoginScreen(
                onBackClick = { navController.popBackStack() },
                onRegisterClick = { navController.navigate("register") },
                // Передаем колбэк для успешного входа
                onLoginSuccess = {
                    navController.navigate("main") {
                        // Очищаем стек, чтобы нельзя было вернуться на форму входа кнопкой "Назад"
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        // Экран РЕГИСТРАЦИИ
        composable("register") {
            RegisterScreen(
                onBackClick = { navController.popBackStack() },
                onLoginClick = { navController.popBackStack() },
                // Передаем колбэк для успешной регистрации
                onRegisterSuccess = {
                    navController.navigate("main") {
                        popUpTo("register") { inclusive = true }
                    }
                }
            )
        }

        // Экран Main (снизу меню)
        composable("main") {
            MainScreen(
                onProfileClick = {
                    navController.navigate("profile")
                }
            )
        }

        // НОВЫЙ ЭКРАН: Личный кабинет
        composable("profile") {
            ProfileScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}