package com.example.civicapp.data.models


data class Mission(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val imageUrl: String? = null,
    val dateMillis: Long = 0L,
    val location: String = "",
    val status: String = "pending", // "pending" | "completed" | "canceled"
    val createdBy: String = "",
    val participantsCount: Int = 0,
    val participants: List<String> = emptyList(),
    val lastUpdatedAt: Long = System.currentTimeMillis()
)






/*
old code
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

 */