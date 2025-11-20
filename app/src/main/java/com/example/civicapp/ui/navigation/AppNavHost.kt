package com.example.civicapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.civicapp.ui.screens.*
import com.example.civicapp.viewModel.*

@Composable
fun AppNavHost() {
    // Step 1: Create a navigation controller
    // This handles moving between screens
    val navController = rememberNavController()

    // Step 2: Create the AuthViewModel
    // This holds login status and user info
    val authViewModel: AuthViewModel = viewModel()

    // Step 3: Read the login status
    // This changes from false -> true when user logs in
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState(initial = false)
    val isLoading by authViewModel.isLoading.collectAsState(initial = true)

    // Step 4: Decide which screen to show first
    // Logic: "If app is loading, show splash. If logged in, show main. Otherwise show login."
    val startDestination = when {
        isLoading -> Routes.SPLASH      // Show splash screen while checking auth
        isLoggedIn -> Routes.MAIN       // User is logged in, show main app
        else -> Routes.LOGIN            // User not logged in, show login
    }

    // Step 5: Set up the navigation structure
    // This defines all screens and how they connect
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Screen 1: Splash Screen
        // Shown for ~2 seconds while app checks if user is logged in
        composable(Routes.SPLASH) {
            SplashScreen(
                onNavigateToLogin = {
                    // When splash finishes and user is NOT logged in:
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.SPLASH) { inclusive = true }  // Remove splash from history
                    }
                },
                onNavigateToMain = {
                    // When splash finishes and user IS logged in:
                    navController.navigate(Routes.MAIN) {
                        popUpTo(Routes.SPLASH) { inclusive = true }  // Remove splash from history
                    }
                },
                authViewModel = authViewModel
            )
        }

        // Screen 2: Login Screen
        // User enters email and password here
        composable(Routes.LOGIN) {
            LoginScreen(
                authViewModel = authViewModel,
                onLoginSuccess = {
                    // After successful login:
                    navController.navigate(Routes.MAIN) {
                        popUpTo(Routes.LOGIN) { inclusive = true }  // User can't go back to login
                    }
                },
                onNavigateToRegister = {
                    // If user clicks "Create Account":
                    navController.navigate(Routes.REGISTER)
                }
            )
        }

        // Screen 3: Register Screen
        // User creates a new account here
        composable(Routes.REGISTER) {
            RegisterScreen(
                authViewModel = authViewModel,
                onRegisterSuccess = {
                    // After successful registration:
                    navController.navigate(Routes.MAIN) {
                        popUpTo(Routes.REGISTER) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    // If user clicks "Back to Login":
                    navController.popBackStack()
                }
            )
        }

        // Screen 4: Main App Screen (with bottom navigation)
        composable(Routes.MAIN) {
            MainScreenWithNav(
                onLogout = {
                    // When user clicks logout in profile:
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.MAIN) { inclusive = true }  // Clear all history
                    }
                }
            )
        }
    }
}