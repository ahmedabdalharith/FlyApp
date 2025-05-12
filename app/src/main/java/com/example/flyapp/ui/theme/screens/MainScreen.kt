package com.example.flyapp.ui.theme.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.flyapp.ui.theme.navigition.BottomNavItem
import com.example.flyapp.ui.theme.navigition.FlightBottomNavigationBar
import com.example.flyapp.ui.theme.navigition.Screen
import com.example.flyapp.ui.theme.theme.GoldColor

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
        modifier = Modifier.fillMaxSize().background(Color.Transparent),
        floatingActionButton = {
            FloatingActionButton(
                containerColor = GoldColor,
                onClick = {
                    mainNavController.navigate(
                        Screen.FlightStatusScreen.route
                    )
                },
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add",
               )
            }

        },
        floatingActionButtonPosition = FabPosition.End,
    ) { innerPadding ->
        NavHost(
            navController = bottomNavController,
            startDestination = BottomNavItem.Home.route,
        ) {
            composable(BottomNavItem.Home.route) {
                HomeScreen( mainNavController)
            }

            composable(BottomNavItem.Trips.route) { TripManagementScreen(mainNavController) }
            composable(BottomNavItem.Offers.route) { AllOffersScreen(mainNavController) }
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