package com.example.civicapp.data.models

data class Participation(
    val id: String = "",
    val missionId: String = "",
    val userId: String = "",
    val joinedAt: Long = System.currentTimeMillis(),
    val leftAt: Long? = null
)