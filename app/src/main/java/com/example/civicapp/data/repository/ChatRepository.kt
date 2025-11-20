// com/example/civicapp/data/repository/ChatRepository.kt
package com.example.civicapp.data.repository

import com.example.civicapp.data.models.Message
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class ChatRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    private val chats = db.collection("chats")

    // --- SEND MESSAGE ---
    suspend fun sendMessage(chatId: String, message: Message): Boolean {
        return try {
            chats.document(chatId).collection("messages").add(message).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    // --- CREATE CHATROOM ---
    suspend fun createChat(user1: String, user2: String): String? {
        return try {
            val chatDoc = chats.document()
            chatDoc.set(
                mapOf(
                    "users" to listOf(user1, user2),
                    "createdAt" to System.currentTimeMillis()
                )
            ).await()
            chatDoc.id
        } catch (e: Exception) {
            null
        }
    }

    // --- LISTEN TO MESSAGES IN REAL TIME ---
    fun listenToMessages(chatId: String): Flow<List<Message>> = callbackFlow {
        val listener = chats.document(chatId)
            .collection("messages")
            .orderBy("timestamp")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val messages = snapshot?.toObjects(Message::class.java) ?: emptyList()
                trySend(messages)
            }

        awaitClose { listener.remove() }
    }
}
