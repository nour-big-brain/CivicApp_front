package com.example.civicapp.data.models

data class NotificationItem(
    val id: String = "",
    val userId: String = "",
    val type: String = "",           // "chat", "mission_update", ...
    val missionId: String? = null,
    val message: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val read: Boolean = false
)