package com.example.civicapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.text.font.FontWeight
import com.example.civicapp.data.models.Notification
import com.example.civicapp.viewModel.NotificationViewModel
import androidx.compose.foundation.lazy.items

@Composable
fun NotificationScreen(notificationViewModel: NotificationViewModel) {
    val notifications by notificationViewModel.notifications.collectAsState(initial = emptyList())

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Correct overload: pass the list
        items(notifications) { notification ->
            NotificationItem(notification)
        }
    }
}

@Composable
fun NotificationItem(notification: Notification) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = notification.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = notification.message,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

/*
@Composable
fun NotificationsScreen() {
    var notifications by remember {
        mutableStateOf(
            listOf(
                NotificationItem("Mission confirmée", "Votre participation à \"Nettoyage du parc\" a été confirmée", "2 min", NotificationType.SUCCESS),
                NotificationItem("Nouvelle mission", "\"Atelier de programmation\" a été créée dans votre catégorie", "15 min", NotificationType.INFO),
                NotificationItem("Rappel", "Votre mission commence demain à 9h00", "1 h", NotificationType.WARNING),
                NotificationItem("Système", "Mise à jour disponible pour l'application", "3 h", NotificationType.INFO),
                NotificationItem("Impact", "Vous avez contribué à 10 heures de bénévolat ce mois!", "1 j", NotificationType.SUCCESS),
            )
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Notifications",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        items(notifications.size) { index ->
            NotificationCard(
                notification = notifications[index],
                onDismiss = {
                    notifications = notifications.filterIndexed { i, _ -> i != index }
                }
            )
        }

        if (notifications.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Aucune notification",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun NotificationCard(
    notification: NotificationItem,
    onDismiss: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = when (notification.type) {
                NotificationType.SUCCESS -> MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                NotificationType.WARNING -> MaterialTheme.colorScheme.tertiary.copy(alpha = 0.1f)
                NotificationType.INFO -> MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)
            }
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = when (notification.type) {
                            NotificationType.SUCCESS -> MaterialTheme.colorScheme.primary
                            NotificationType.WARNING -> MaterialTheme.colorScheme.tertiary
                            NotificationType.INFO -> MaterialTheme.colorScheme.secondary
                        },
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (notification.type) {
                        NotificationType.SUCCESS -> Icons.Default.Check
                        NotificationType.WARNING -> Icons.Default.Warning
                        NotificationType.INFO -> Icons.Default.Info
                    },
                    contentDescription = "Notification",
                    tint = MaterialTheme.colorScheme.background,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Content
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = notification.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = notification.message,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            }

            // Time and Delete
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = notification.time,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                )
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Supprimer",
                        tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

data class NotificationItem(
    val title: String,
    val message: String,
    val time: String,
    val type: NotificationType
)

enum class NotificationType {
    SUCCESS, WARNING, INFO
}

 */