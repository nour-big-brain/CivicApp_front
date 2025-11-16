package com.example.civicapp.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun BottomNavigationBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    NavigationBar {
        // Home Tab
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            label = { Text("Home") },
            selected = selectedTab == 0,
            onClick = { onTabSelected(0) }
        )

        // Missions Tab
        NavigationBarItem(
            icon = { Icon(Icons.Default.List, contentDescription = null) },
            label = { Text("Missions") },
            selected = selectedTab == 1,
            onClick = { onTabSelected(1) }
        )

        // Messaging Tab
        NavigationBarItem(
            icon = { Icon(Icons.Default.Mail, contentDescription = null) },
            label = { Text("Chats") },
            selected = selectedTab == 2,
            onClick = { onTabSelected(2) }
        )

        // Notifications Tab
        NavigationBarItem(
            icon = { Icon(Icons.Default.Notifications, contentDescription = null) },
            label = { Text("Alerts") },
            selected = selectedTab == 3,
            onClick = { onTabSelected(3) }
        )

        // Participation Tab
        NavigationBarItem(
            icon = { Icon(Icons.Default.BookmarkBorder, contentDescription = null) },
            label = { Text("My Tasks") },
            selected = selectedTab == 4,
            onClick = { onTabSelected(4) }
        )

        // Profile Tab
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = null) },
            label = { Text("Profile") },
            selected = selectedTab == 5,
            onClick = { onTabSelected(5) }
        )
    }
}
