package org.neteinstein.snap2sheet.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.neteinstein.snap2sheet.ui.screens.destination.DestinationScreen
import org.neteinstein.snap2sheet.ui.screens.history.HistoryScreen
import org.neteinstein.snap2sheet.ui.screens.home.HomeScreen
import org.neteinstein.snap2sheet.ui.screens.review.ReviewScreen
import org.neteinstein.snap2sheet.ui.screens.scan.ScanScreen
import org.neteinstein.snap2sheet.ui.screens.settings.SettingsScreen
import org.neteinstein.snap2sheet.ui.screens.signin.SignInScreen
import org.neteinstein.snap2sheet.ui.screens.welcome.WelcomeScreen

/** Mirrors the Fatura design's eight screens 1:1 — see project/canvas.json in the design source. */
sealed class Screen(val route: String) {
    data object Welcome : Screen("welcome")
    data object SignIn : Screen("signin")
    data object Home : Screen("home")
    data object Scan : Screen("scan")
    data object Review : Screen("review")
    data object Destination : Screen("destination")
    data object History : Screen("history")
    data object Settings : Screen("settings")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Welcome.route) {
        composable(Screen.Welcome.route) {
            WelcomeScreen(onContinue = { navController.navigate(Screen.SignIn.route) })
        }

        composable(Screen.SignIn.route) {
            SignInScreen(
                onBack = { navController.popBackStack() },
                onSignedIn = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                },
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onScan = { navController.navigate(Screen.Scan.route) },
                onHistory = { navController.navigate(Screen.History.route) },
                onSettings = { navController.navigate(Screen.Settings.route) },
            )
        }

        composable(Screen.Scan.route) {
            ScanScreen(
                onBack = { navController.popBackStack() },
                onCaptured = { navController.navigate(Screen.Review.route) },
            )
        }

        composable(Screen.Review.route) {
            ReviewScreen(
                onBack = { navController.popBackStack() },
                onChooseDestination = { navController.navigate(Screen.Destination.route) },
                onSave = { navController.navigate(Screen.Destination.route) },
            )
        }

        composable(Screen.Destination.route) {
            DestinationScreen(
                onBack = { navController.popBackStack() },
                onSaved = {
                    navController.popBackStack(Screen.Home.route, inclusive = false)
                },
            )
        }

        composable(Screen.History.route) {
            HistoryScreen(onBack = { navController.popBackStack() })
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                onBack = { navController.popBackStack() },
                onChangeDefaultSpreadsheet = { navController.navigate(Screen.Destination.route) },
            )
        }
    }
}
