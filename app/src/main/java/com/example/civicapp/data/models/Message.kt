package com.example.civicapp.data.models

data class Message(
    val senderId: String = "",
    val senderName: String = "",
    val content: String = "",
    val timestamp: Long = System.currentTimeMillis()
)