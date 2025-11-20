package com.example.civicapp.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.civicapp.viewModel.AuthViewModel
import com.example.civicapp.viewModel.ChatViewModel
import com.example.civicapp.viewModel.MissionViewModel
import com.example.civicapp.viewModel.NotificationViewModel

@Composable
fun MainScreenWithNav(
    onLogout: () -> Unit
) {
    // Get all the ViewModels we need
    val authViewModel: AuthViewModel = viewModel()
    val missionViewModel: MissionViewModel = viewModel()
    val chatViewModel: ChatViewModel = viewModel()
    val notificationViewModel: NotificationViewModel = viewModel()

    // Remember which tab is selected (0 = Home, 1 = Missions, etc)
    var selectedTab by remember { mutableIntStateOf(0) }

    // Get data from ViewModels
    val missions by missionViewModel.missions.collectAsState(initial = emptyList())
    val currentUser by authViewModel.user.collectAsState()

    // Load missions when this screen first appears
    LaunchedEffect(Unit) {
        missionViewModel.loadAllMissions()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Main content area (takes up most of screen)
        Box(modifier = Modifier.weight(1f)) {
            ScreenContent(
                selectedTab = selectedTab,
                authViewModel = authViewModel,
                missionViewModel = missionViewModel,
                chatViewModel = chatViewModel,
                notificationViewModel = notificationViewModel,
                missionsList = missions,
                currentUser = currentUser,
                onLogout = onLogout
            )
        }

        // Bottom navigation bar (always at bottom)
        BottomNavigationBar(
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it }
        )
    }
}
