// com/example/civicapp/viewModel/ChatViewModel.kt
package com.example.civicapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.civicapp.data.models.Message
import com.example.civicapp.data.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ChatViewModel(
    private val chatRepository: ChatRepository = ChatRepository()
) : ViewModel() {

    // Get messages flow for a given chatId
    fun getMessages(chatId: String): Flow<List<Message>> {
        return chatRepository.listenToMessages(chatId)
    }

    // Send a message to a given chatId
    fun sendMessage(
        chatId: String,
        senderId: String,
        senderName: String,
        content: String
    ) {
        val message = Message(
            senderId = senderId,
            senderName = senderName,
            content = content,
            timestamp = System.currentTimeMillis()
        )

        viewModelScope.launch {
            chatRepository.sendMessage(chatId, message)
        }
    }

    // Optional helper: create a chat between two users
    fun createChat(
        user1Id: String,
        user2Id: String,
        onResult: (String?) -> Unit
    ) {
        viewModelScope.launch {
            val chatId = chatRepository.createChat(user1Id, user2Id)
            onResult(chatId)
        }
    }
}
