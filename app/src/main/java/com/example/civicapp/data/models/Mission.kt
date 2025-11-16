package com.example.civicapp.data.models

data class Mission(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val category: String = "",
    val date: String = "",
    val location: String = "",
    val difficulty: String = "",
    val createdBy: String = "",
    val participants: List<String> = emptyList(),
    val maxParticipants: Int = 0,
    val status: String = "upcoming",
    val createdAt: Long = 0
)