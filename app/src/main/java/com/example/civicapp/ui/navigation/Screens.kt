package com.example.civicapp.ui.navigation

import androidx.compose.runtime.Composable
import com.example.civicapp.data.models.Mission
import com.example.civicapp.data.models.User
import com.example.civicapp.ui.screens.HomeScreen
import com.example.civicapp.ui.screens.MissionsScreen
import com.example.civicapp.ui.screens.MessagingScreen
import com.example.civicapp.ui.screens.NotificationScreen
import com.example.civicapp.ui.screens.ParticipationScreen
import com.example.civicapp.ui.screens.ProfileScreen
import com.example.civicapp.viewModel.AuthViewModel
import com.example.civicapp.viewModel.ChatViewModel
import com.example.civicapp.viewModel.MissionViewModel
import com.example.civicapp.viewModel.NotificationViewModel

@Composable
fun ScreenContent(
    selectedTab: Int,
    authViewModel: AuthViewModel,
    missionViewModel: MissionViewModel,
    chatViewModel: ChatViewModel,
    notificationViewModel: NotificationViewModel,
    missionsList: List<Mission>,
    currentUser: User?,
    onLogout: () -> Unit
) {
    // Show different screen based on which tab is selected
    when (selectedTab) {
        0 -> HomeScreen(
            missionViewModel = missionViewModel,
            authViewModel = authViewModel
        )

        1 -> MissionsScreen(
            missionViewModel = missionViewModel
        )

        2 -> MessagingScreen(
            authViewModel = authViewModel,
            chatViewModel = chatViewModel,
            chatroomId = currentUser?.id ?: "default"
        )

        3 -> NotificationScreen(
            notificationViewModel = notificationViewModel
        )

        4 -> ParticipationScreen(
            missions = missionsList
        )

        5 -> ProfileScreen(
            authViewModel = authViewModel,
            onLogout = onLogout
        )

        else -> HomeScreen(
            missionViewModel = missionViewModel,
            authViewModel = authViewModel
        )
    }
}


/*
@Composable
fun ScreenContent(selectedTab: Int) {
    when (selectedTab) {
        0 -> HomeScreen()
        1 -> MissionsScreen()
        2 -> MessagingScreen()
        3 -> NotificationScreen()
        4 -> ParticipationScreen()
        5 -> ProfileScreen()
        else -> HomeScreen()
    }
}

 */