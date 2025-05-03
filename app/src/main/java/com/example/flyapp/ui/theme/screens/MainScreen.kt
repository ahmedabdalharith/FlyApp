package com.example.flyapp.ui.theme.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.flyapp.ui.theme.components.BottomNavItem
import com.example.flyapp.ui.theme.components.FlightBottomNavigationBar
import com.example.flyapp.ui.theme.navigition.Screen

@Composable
fun MainScreen(mainNavController: NavHostController) {
    val bottomNavController = rememberNavController()
    val mockNotifications = mapOf(
        Screen.OffersScreen.route to 3,
        Screen.TripManagementScreen.route to 7
    )
    Scaffold(
        bottomBar = { FlightBottomNavigationBar(bottomNavController,
            notificationCounts=mockNotifications) },
        modifier = Modifier.fillMaxSize().background(Color.Transparent)
    ) { innerPadding ->
        NavHost(
            navController = bottomNavController,
            startDestination = BottomNavItem.Home.route,
        ) {
            composable(BottomNavItem.Home.route) {
                HomeScreen( mainNavController)
            }

            composable(BottomNavItem.Trips.route) { TripManagementScreen(mainNavController) }
            composable(BottomNavItem.Offers.route) { OffersScreen(mainNavController) }
            composable(BottomNavItem.Search.route) { SearchFlightScreen(mainNavController) }
            composable(BottomNavItem.SettingsScreen.route) { SettingsScreen(mainNavController) }
        }
    }
}


@Preview
@Composable
fun MainScreenPreview(){
    MainScreen(rememberNavController())
}