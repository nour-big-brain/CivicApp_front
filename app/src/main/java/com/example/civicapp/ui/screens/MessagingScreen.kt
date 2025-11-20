package com.example.civicapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.civicapp.data.models.Message
import com.example.civicapp.viewModel.AuthViewModel
import com.example.civicapp.viewModel.ChatViewModel

@Composable
fun MessagingScreen(
    authViewModel: AuthViewModel,
    chatViewModel: ChatViewModel,
    chatroomId: String
) {
    // Get current user ID from AuthViewModel
    val currentUserId by remember {
        mutableStateOf(authViewModel.getCurrentUserId() ?: "")
    }
    val currentUserName by remember {
        mutableStateOf(authViewModel.getCurrentUserName() ?: "Unknown")
    }

    // Collect messages as state from Flow<List<Message>>
    val messages by chatViewModel
        .getMessages(chatroomId)
        .collectAsState(initial = emptyList())

    var newMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Chat",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Messages List
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            reverseLayout = true
        ) {
            items(messages.size) { index ->
                val message = messages[messages.size - 1 - index]
                MessageItem(message = message, currentUserId = currentUserId)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Input field and send button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            BasicTextField(
                value = newMessage,
                onValueChange = { newMessage = it },
                modifier = Modifier
                    .weight(1f)
                    .background(Color(0xFFEFEFEF), shape = MaterialTheme.shapes.small)
                    .padding(8.dp),
                singleLine = true
            )
            Button(
                onClick = {
                    if (newMessage.isNotBlank() && currentUserId.isNotBlank()) {
                        chatViewModel.sendMessage(
                            chatId = chatroomId,
                            senderId = currentUserId,
                            senderName = currentUserName,
                            content = newMessage
                        )
                        newMessage = ""
                    }
                }
            ) {
                Text("Envoyer")
            }
        }
    }
}

@Composable
fun MessageItem(message: Message, currentUserId: String) {
    val isMine = message.senderId == currentUserId

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isMine) Arrangement.End else Arrangement.Start
    ) {
        Surface(
            color = if (isMine) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
            shape = MaterialTheme.shapes.medium,
            shadowElevation = 2.dp,
            modifier = Modifier.padding(4.dp)
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = message.senderName,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isMine) Color.White else MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = message.content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isMine) Color.White else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
/*
@Composable
fun MessagingScreen() {
    var searchQuery by remember { mutableStateOf("") }
    var selectedChat by remember { mutableStateOf<ChatUser?>(null) }

    if (selectedChat == null) {
        ChatListView(
            searchQuery = searchQuery,
            onSearchChange = { searchQuery = it },
            onChatSelect = { selectedChat = it }
        )
    } else {
        ChatDetailView(
            chatUser = selectedChat!!,
            onBackPress = { selectedChat = null }
        )
    }
}

@Composable
fun ChatListView(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    onChatSelect: (ChatUser) -> Unit
) {
    val chatList = remember {
        listOf(
            ChatUser("Ahmed Ben Ali", "Oui, à demain!", "2 min", ""),
            ChatUser("Fatima Saïdi", "Merci pour votre aide!", "15 min", ""),
            ChatUser("Groupe Environnement", "Réunion d'organisation", "1 h", "👥"),
            ChatUser("Mohamed Jarray", "Comment ça va?", "3 h", ""),
            ChatUser("Équipe Nettoyage", "Les fournitures sont prêtes", "1 j", "👥"),
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Messages",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                placeholder = { Text("Chercher un message") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Chercher")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(chatList.size) { index ->
                ChatListItem(
                    chatUser = chatList[index],
                    onClick = { onChatSelect(chatList[index]) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun ChatListItem(chatUser: ChatUser, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(12.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = chatUser.name.first().toString(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            // Content
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = chatUser.name + (if (chatUser.isGroup.isNotEmpty()) " ${chatUser.isGroup}" else ""),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = chatUser.time,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
                Text(
                    text = chatUser.lastMessage,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun ChatDetailView(chatUser: ChatUser, onBackPress: () -> Unit) {
    var messageText by remember { mutableStateOf("") }
    val messages = remember {
        listOf(
            Message("Salut, ça va?", true, "10:30"),
            Message("Oui, bien et toi?", false, "10:31"),
            Message("Je vais bien, tu viens demain?", true, "10:32"),
            Message("Oui, à quelle heure?", false, "10:33"),
            Message("À 9h du matin au parc", true, "10:34"),
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackPress) {
                Text("←", fontSize = 24.sp, color = MaterialTheme.colorScheme.onPrimary)
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = chatUser.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Text(
                    text = "Actif maintenant",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                )
            }
        }

        // Messages List
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            reverseLayout = true
        ) {
            items(messages.size) { index ->
                MessageBubble(message = messages[messages.size - 1 - index])
            }
        }

        // Input Area
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = messageText,
                onValueChange = { messageText = it },
                placeholder = { Text("Votre message...") },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                singleLine = true
            )
            IconButton(
                onClick = { messageText = "" },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Envoyer",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun MessageBubble(message: Message) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = if (message.isCurrentUser) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(
                    color = if (message.isCurrentUser)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.surfaceVariant
                )
                .padding(12.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = message.text,
                    fontSize = 14.sp,
                    color = if (message.isCurrentUser)
                        MaterialTheme.colorScheme.onPrimary
                    else
                        MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = message.time,
                    fontSize = 10.sp,
                    color = if (message.isCurrentUser)
                        MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                    else
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
        }
    }
}

data class ChatUser(
    val name: String,
    val lastMessage: String,
    val time: String,
    val isGroup: String = ""
)

data class Message(
    val text: String,
    val isCurrentUser: Boolean,
    val time: String
)
 */