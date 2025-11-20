package com.example.civicapp.data.models


data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val bio: String = "",
    val profileImageUrl: String? = null,
    val joinedAt: Long = System.currentTimeMillis(),
    val points: Int = 0,
    val activeMissions: List<String> = emptyList()
)