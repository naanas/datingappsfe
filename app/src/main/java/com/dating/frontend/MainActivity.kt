package com.dating.frontend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dating.frontend.ui.FeedScreen
import com.dating.frontend.ui.LoginScreen
import com.dating.frontend.ui.RegisterScreen // Import RegisterScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Setup Navigasi
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "login") {
                composable("login") {
                    LoginScreen(navController)
                }
                // Tambahkan rute register di sini
                composable("register") {
                    RegisterScreen(navController)
                }
                composable("feed") {
                    FeedScreen(navController)
                }
            }
        }
    }
}