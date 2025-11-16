package com.example.civicapp.ui.navigation

import androidx.compose.runtime.Composable
import com.example.civicapp.ui.screens.HomeScreen
import com.example.civicapp.ui.screens.MissionsScreen
import com.example.civicapp.ui.screens.MessagingScreen
import com.example.civicapp.ui.screens.NotificationsScreen
import com.example.civicapp.ui.screens.ParticipationScreen
import com.example.civicapp.ui.screens.ProfileScreen

@Composable
fun ScreenContent(selectedTab: Int) {
    when (selectedTab) {
        0 -> HomeScreen()
        1 -> MissionsScreen()
        2 -> MessagingScreen()
        3 -> NotificationsScreen()
        4 -> ParticipationScreen()
        5 -> ProfileScreen()
        else -> HomeScreen()
    }
}