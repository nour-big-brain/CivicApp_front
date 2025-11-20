package com.example.civicapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.civicapp.data.models.Mission
import com.example.civicapp.ui.utils.DateTimeUtils
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*

@Composable
fun ParticipationScreen(
    missions: List<Mission> // You can provide your missions list from a ViewModel
) {
    var selectedTab by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Mes Participations",
                fontSize = 28.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Suivez vos missions en cours et passées",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )
        }

        // Tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TabButton("En cours", selectedTab == 0) { selectedTab = 0 }
            TabButton("Complétées", selectedTab == 1) { selectedTab = 1 }
            TabButton("Annulées", selectedTab == 2) { selectedTab = 2 }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Content
        Box(modifier = Modifier.weight(1f)) {
            when (selectedTab) {
                0 -> MissionsTab(missions.filter { it.status == "pending" })
                1 -> MissionsTab(missions.filter { it.status == "completed" })
                2 -> MissionsTab(missions.filter { it.status == "canceled" })
            }
        }
    }
}

@Composable
fun TabButton(label: String, isSelected: Boolean, onClick: () -> Unit) {
    if (isSelected) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
            shape = RoundedCornerShape(8.dp)
        ) { Text(label, fontSize = 12.sp) }
    } else {
        OutlinedButton(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
            shape = RoundedCornerShape(8.dp)
        ) { Text(label, fontSize = 12.sp) }
    }
}

@Composable
fun MissionsTab(missions: List<Mission>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(missions.size) { index ->
            ParticipationCard(missions[index])
        }
        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}

@Composable
fun ParticipationCard(mission: Mission) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Title & Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = mission.title,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                Box(
                    modifier = Modifier
                        .background(
                            color = when (mission.status) {
                                "pending" -> MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f)
                                "completed" -> MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                                "canceled" -> MaterialTheme.colorScheme.error.copy(alpha = 0.2f)
                                else -> MaterialTheme.colorScheme.surfaceVariant
                            },
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = mission.status.replaceFirstChar { it.uppercase() },
                        fontSize = 11.sp,
                        color = when (mission.status) {
                            "pending" -> MaterialTheme.colorScheme.tertiary
                            "completed" -> MaterialTheme.colorScheme.primary
                            "canceled" -> MaterialTheme.colorScheme.error
                            else -> MaterialTheme.colorScheme.onSurface
                        }
                    )
                }
            }

            // Date & Location
            Text(
                text = "📅 ${DateTimeUtils.formatDateTime(mission.dateMillis)}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Text(
                text = "📍 ${mission.location}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )

            // Participants
            if (mission.participantsCount > 0) {
                Text(
                    text = "👥 ${mission.participantsCount} participant(s)",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { /* Withdraw logic */ },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) { Text("Se retirer", fontSize = 12.sp) }

                Button(
                    onClick = { /* Details logic */ },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) { Text("Détails", fontSize = 12.sp) }
            }
        }
    }
}


/*
@Composable
fun ParticipationScreen() {
    var selectedTab by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Mes Participations",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Suivez vos missions en cours et passées",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )
        }

        // Tab Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TabButton(
                label = "En cours",
                isSelected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                modifier = Modifier.weight(1f)
            )
            TabButton(
                label = "Complétées",
                isSelected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                modifier = Modifier.weight(1f)
            )
            TabButton(
                label = "Annulées",
                isSelected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Content
        Box(modifier = Modifier.weight(1f)) {
            when (selectedTab) {
                0 -> OngoingMissionsTab()
                1 -> CompletedMissionsTab()
                2 -> CancelledMissionsTab()
            }
        }
    }
}

@Composable
fun TabButton(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (isSelected) {
        Button(
            onClick = onClick,
            modifier = modifier.height(40.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(label, fontSize = 12.sp)
        }
    } else {
        OutlinedButton(
            onClick = onClick,
            modifier = modifier.height(40.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(label, fontSize = 12.sp)
        }
    }
}

@Composable
fun OngoingMissionsTab() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(3) { index ->
            when (index) {
                0 -> ParticipationCard(
                    title = "Nettoyage du parc",
                    date = "25 Nov 2024",
                    time = "09:00 - 12:00",
                    location = "Parc Central",
                    status = "En cours",
                    progress = 60,
                    participants = 8,
                    canWithdraw = true
                )
                1 -> ParticipationCard(
                    title = "Atelier de programmation",
                    date = "28 Nov 2024",
                    time = "14:00 - 17:00",
                    location = "Centre Communautaire",
                    status = "Confirmée",
                    progress = 0,
                    participants = 12,
                    canWithdraw = true
                )
                2 -> ParticipationCard(
                    title = "Distribution d'aide alimentaire",
                    date = "02 Déc 2024",
                    time = "10:00 - 14:00",
                    location = "Quartier Nord",
                    status = "Confirmée",
                    progress = 0,
                    participants = 15,
                    canWithdraw = true
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun CompletedMissionsTab() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(2) { index ->
            when (index) {
                0 -> ParticipationCard(
                    title = "Plantation d'arbres",
                    date = "15 Nov 2024",
                    time = "09:00 - 12:00",
                    location = "Parc du Nord",
                    status = "Complétée",
                    progress = 100,
                    participants = 6,
                    hoursEarned = 3,
                    canWithdraw = false
                )
                1 -> ParticipationCard(
                    title = "Visite aux personnes âgées",
                    date = "10 Nov 2024",
                    time = "15:00 - 17:00",
                    location = "Maison de retraite",
                    status = "Complétée",
                    progress = 100,
                    participants = 4,
                    hoursEarned = 2,
                    canWithdraw = false
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun CancelledMissionsTab() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(1) { index ->
            ParticipationCard(
                title = "Nettoyage de la plage",
                date = "20 Nov 2024",
                time = "08:00 - 11:00",
                location = "Plage Centrale",
                status = "Annulée",
                progress = 0,
                participants = 0,
                canWithdraw = false
            )
        }

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Vous n'avez annulé qu'une seule mission",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun ParticipationCard(
    title: String,
    date: String,
    time: String,
    location: String,
    status: String,
    progress: Int,
    participants: Int,
    hoursEarned: Int = 0,
    canWithdraw: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Title and Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                Box(
                    modifier = Modifier
                        .background(
                            color = when (status) {
                                "En cours" -> MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f)
                                "Confirmée" -> MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)
                                "Complétée" -> MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                                else -> MaterialTheme.colorScheme.error.copy(alpha = 0.2f)
                            },
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = status,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = when (status) {
                            "En cours" -> MaterialTheme.colorScheme.tertiary
                            "Confirmée" -> MaterialTheme.colorScheme.secondary
                            "Complétée" -> MaterialTheme.colorScheme.primary
                            else -> MaterialTheme.colorScheme.error
                        }
                    )
                }
            }

            // Date and Time
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Date",
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Text(
                        text = date,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
                Text(
                    text = time,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }

            // Location
            Text(
                text = "📍 $location",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )

            // Progress Bar (if applicable)
            if (progress > 0) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(3.dp)
                        )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progress / 100f)
                            .fillMaxSize()
                            .background(
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(3.dp)
                            )
                    )
                }
                Text(
                    text = "$progress% complétée",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            // Stats Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (hoursEarned > 0) {
                    Text(
                        text = "✓ $hoursEarned h gagnées",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                if (participants > 0) {
                    Text(
                        text = "👥 $participants participant(s)",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }

            // Action Buttons
            if (canWithdraw) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Se retirer", fontSize = 12.sp)
                    }
                    Button(
                        onClick = { },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Détails", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

 */