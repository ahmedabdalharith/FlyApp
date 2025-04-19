package com.example.flyapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.example.flyapp.ui.theme.screens.MainScreen

@Composable
fun FlightBookingApp() {
    val navController = rememberNavController()
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF0288D1),
            secondary = Color(0xFF26C6DA),
            background = Color.White,
            surface = Color.White,
            onPrimary = Color.White,
            onSecondary = Color.Black,
            onBackground = Color.Black,
            onSurface = Color.Black
        ),
    ) {
    }
}
