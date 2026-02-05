package com.example.wackamoleadvanced

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = androidx.compose.ui.Modifier.fillMaxSize()) {
                    AppNav()
                }
            }
        }
    }
}

@Composable
fun AppNav() {
    val nav = rememberNavController()

    NavHost(
        navController = nav,
        startDestination = "login"
    ) {

        // Login
        composable("login") {
            LoginScreen(
                onLogin = { userId ->
                    nav.navigate("game/$userId")
                },
                onSignup = {
                    nav.navigate("signup")
                }
            )
        }

        // Signup
        composable("signup") {
            SignupScreen(
                onCreated = { userId ->
                    nav.navigate("game/$userId")
                },
                onBack = {
                    nav.popBackStack()
                }
            )
        }

        // Game
        composable("game/{userId}") { backStack ->
            val userId =
                backStack.arguments?.getString("userId")?.toLongOrNull() ?: -1L

            GameScreen(
                userId = userId,
                onOpenSettings = {
                    nav.navigate("settings")
                },
                onOpenLeaderboard = {
                    nav.navigate("leaderboard/$userId")
                }
            )
        }

        // Settings
        composable("settings") {
            SettingsScreen(
                onBack = { nav.popBackStack() },
                onLogout = {
                    nav.navigate("login") {
                        popUpTo(0)
                    }
                }
            )
        }

        // Leaderboard
        composable("leaderboard/{userId}") { backStack ->
            val userId =
                backStack.arguments?.getString("userId")?.toLongOrNull() ?: -1L

            LeaderboardScreen(
                userId = userId,
                onBack = {
                    nav.popBackStack()
                }
            )
        }
    }
}