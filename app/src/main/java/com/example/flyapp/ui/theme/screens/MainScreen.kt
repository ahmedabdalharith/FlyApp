package com.example.flyapp.ui.theme.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.flyapp.ui.theme.components.BottomNavItem
import com.example.flyapp.ui.theme.components.FlightBottomNavigationBar

@Composable
fun MainScreen(mainNavController: NavHostController) {
    val bottomNavController = rememberNavController()

    Scaffold(
        bottomBar = { FlightBottomNavigationBar(bottomNavController) },
        modifier = Modifier.fillMaxSize().background(Color.Transparent)
    ) { innerPadding ->
        NavHost(
            navController = bottomNavController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Home.route) {
                HomeScreen( mainNavController)
            }

            composable(BottomNavItem.Trips.route) { TripManagementScreen(mainNavController) }
            composable(BottomNavItem.Offers.route) { OffersScreen(mainNavController) }
            composable(BottomNavItem.HelpCenter.route) { HelpCenterScreen(mainNavController) }
        }
    }
}


@Preview
@Composable
fun MainScreenPreview(){
    MainScreen(rememberNavController())
}