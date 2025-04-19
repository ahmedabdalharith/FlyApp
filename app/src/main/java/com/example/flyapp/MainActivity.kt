package com.example.flyapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.flyapp.ui.theme.FlightBookingApp
import com.example.flyapp.ui.theme.navigition.FlightBookingNavGraph
import com.example.flyapp.ui.theme.screens.MainScreen
import com.example.flyapp.ui.theme.screens.SplashScreen
import com.example.flyapp.ui.theme.theme.FlyAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlyAppTheme {
                val navController = rememberNavController()
                FlightBookingNavGraph(navController)
            }
        }
    }
}
