package com.example.flyapp.ui.theme.navigition

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.flyapp.ui.theme.screens.AirplaneSeatsScreen
import com.example.flyapp.ui.theme.screens.AllDestinationsScreen
import com.example.flyapp.ui.theme.screens.AllOffersScreen
import com.example.flyapp.ui.theme.screens.SearchFlightScreen
import com.example.flyapp.ui.theme.screens.FeedbackFormScreen
import com.example.flyapp.ui.theme.screens.TicketScreen
import com.example.flyapp.ui.theme.screens.FlightDetailsScreen
import com.example.flyapp.ui.theme.screens.FlightStatusScreen
import com.example.flyapp.ui.theme.screens.ForgotPasswordScreen
import com.example.flyapp.ui.theme.screens.HelpCenterScreen
import com.example.flyapp.ui.theme.screens.LoginScreen
import com.example.flyapp.ui.theme.screens.MainScreen
import com.example.flyapp.ui.theme.screens.NotificationScreen
import com.example.flyapp.ui.theme.screens.OfferDetailsScreen
import com.example.flyapp.ui.theme.screens.PaymentScreen
import com.example.flyapp.ui.theme.screens.ProfileEditorScreen
import com.example.flyapp.ui.theme.screens.ProfileScreen
import com.example.flyapp.ui.theme.screens.SearchResultsScreen
import com.example.flyapp.ui.theme.screens.SettingsScreen
import com.example.flyapp.ui.theme.screens.SignUpScreen
import com.example.flyapp.ui.theme.screens.SplashScreen
import com.example.flyapp.ui.theme.screens.WelcomeScreen


sealed class Screen(val route: String) {
    data object AirplaneSeatsScreen : Screen("AirplaneSeatsScreen")
    data object ProfileEditorScreen : Screen("ProfileEditorScreen")
    data object FeedbackFormScreen : Screen("FeedbackFormScreen")
    data  object FlightDetailsScreen : Screen("FlightDetailsScreen")
    data  object HelpCenterScreen : Screen("HelpCenterScreen")
    data object HomeScreen : Screen("HomeScreen")
    data object LoginScreen : Screen("LoginScreen")
    data object MainScreen : Screen("MainScreen")
    data object OffersScreen : Screen("OffersScreen")
    data object PaymentScreen : Screen("PaymentScreen")
    data object SettingsScreen : Screen("SettingsScreen")
    data object SignUpScreen : Screen("SignUpScreen")
    data  object SplashScreen : Screen("SplashScreen")
    data  object TicketScreen : Screen("TicketScreen")
    data  object TripManagementScreen : Screen("TripManagementScreen")
    data object WelcomeScreen : Screen("WelcomeScreen")
    data object SearchResultsScreen : Screen("SearchResultsScreen")
    data object NotificationScreen : Screen("NotificationScreen")
    data object ProfileScreen : Screen("ProfileScreen")
    data object AllDestinationsScreen : Screen("AllDestinationsScreen")
    data object AllOffersScreen : Screen("AllOffersScreen")
    data object OfferDetailsScreen : Screen("OfferDetailsScreen")
    data object FlightStatusScreen : Screen("FlightStatusScreen")
    data object ForgotPasswordScreen : Screen("ForgetPasswordScreen")
    data object SearchFlightScreen : Screen("SearchFlightScreen")

}

@Composable
fun FlightBookingNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.SplashScreen.route) {

        // Auth flow
        composable(Screen.SplashScreen.route) { SplashScreen(navController) }
        composable(Screen.WelcomeScreen.route) { WelcomeScreen(navController) }
        composable(Screen.LoginScreen.route) { LoginScreen(navController) }
        composable(Screen.SignUpScreen.route) { SignUpScreen(navController) }


        // Main screen with BottomNav
        composable(Screen.MainScreen.route) {
            MainScreen(mainNavController = navController)
        }

        composable(Screen.FlightDetailsScreen.route) { FlightDetailsScreen(navController=navController) }
        composable(Screen.TicketScreen.route) { TicketScreen(navController) }
        composable(Screen.SearchFlightScreen.route) { SearchFlightScreen(navController) }
        composable(Screen.AirplaneSeatsScreen.route) { AirplaneSeatsScreen(navController) }
        composable(Screen.SettingsScreen.route) { SettingsScreen(navController) }
        composable(Screen.PaymentScreen.route) { PaymentScreen(navController) }
        composable(Screen.HelpCenterScreen.route) { HelpCenterScreen(navController) }
        composable(Screen.SearchResultsScreen.route) { SearchResultsScreen(navController) }
        composable(Screen.NotificationScreen.route) { NotificationScreen(navController) }
        composable(Screen.ProfileScreen.route) { ProfileScreen(navController) }
        composable(Screen.AllDestinationsScreen.route) { AllDestinationsScreen(navController) }
        composable(Screen.AllOffersScreen.route) { AllOffersScreen(navController) }
        composable(Screen.OfferDetailsScreen.route) { OfferDetailsScreen(navController,"paris_spring") }
        composable(Screen.FlightStatusScreen.route) { FlightStatusScreen(navController) }
        composable(Screen.ForgotPasswordScreen.route) { ForgotPasswordScreen(navController) }
        composable(Screen.FeedbackFormScreen.route) { FeedbackFormScreen(navController) }
        composable(Screen.ProfileEditorScreen.route) { ProfileEditorScreen(navController) }


    }
}

@Preview
@Composable
fun FlightBookingNavGraphPreview() {
    // Preview the navigation graph
    FlightBookingNavGraph(navController = NavHostController(context = LocalContext.current))
}


