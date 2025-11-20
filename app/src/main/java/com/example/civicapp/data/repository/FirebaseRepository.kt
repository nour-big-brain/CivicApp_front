package com.example.civicapp.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class FirebaseRepository(
    val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    // --- USER HELPERS ---
    fun getCurrentUserId(): String? = auth.currentUser?.uid

    fun isUserLoggedIn(): Boolean = auth.currentUser != null

    fun signOut() = auth.signOut()

    val usersCollection = db.collection("users")
    val missionsCollection = db.collection("missions")
    val chatsCollection = db.collection("chats")

}

/*
old code
class FirebaseRepository {
    private val db = Firebase.firestore
    private val auth = FirebaseAuth.getInstance()

    // Get current user ID
    fun getCurrentUserId(): String? = auth.currentUser?.uid

    // Check if user is logged in
    fun isUserLoggedIn(): Boolean = auth.currentUser != null

    // Sign up new user
    suspend fun signUp(email: String, password: String, name: String) {
        auth.createUserWithEmailAndPassword(email, password).await()
        val userId = auth.currentUser?.uid ?: return

        // Create user document
        val userData = mapOf(
            "name" to name,
            "email" to email,
            "joinedDate" to System.currentTimeMillis(),
            "completedMissions" to emptyList<String>(),
            "totalHours" to 0,
            "badges" to emptyList<String>()
        )

        db.collection("users").document(userId).set(userData).await()
    }

    // Sign in user
    suspend fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).await()
    }

    // Sign out
    fun signOut() {
        auth.signOut()
    }

    // Get all missions
    suspend fun getMissions() {
        // Will implement in next step
    }

    // Get user data
    suspend fun getUserData(userId: String) {
        // Will implement in next step
    }
}

 */