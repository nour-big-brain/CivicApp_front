package com.example.civicapp.data.repository

import com.example.civicapp.data.models.Notification
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class NotificationRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    private val notificationsCollection = db.collection("notifications")

    suspend fun getNotifications(): List<Notification> {
        return try {
            notificationsCollection.get().await().documents.mapNotNull { it.toObject(Notification::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
