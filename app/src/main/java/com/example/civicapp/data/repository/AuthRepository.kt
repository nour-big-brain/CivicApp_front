package com.example.civicapp.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlin.jvm.java
import com.example.civicapp.data.models.User

class AuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    // --- AUTH STATUS ---
    fun getCurrentUserId(): String? = auth.currentUser?.uid
    fun isUserLoggedIn(): Boolean = auth.currentUser != null

    // --- SIGN UP ---
    suspend fun signUp(name: String, email: String, password: String): Result<Unit> {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            val userId = auth.currentUser?.uid ?: return Result.failure(Exception("User ID not found"))

            val userData = mapOf(
                "name" to name,
                "email" to email,
                "createdAt" to System.currentTimeMillis(),
                "bio" to "",
                "age" to 0,
                "phone" to "",
                "profileImage" to "",
                "completedMissions" to 0,
                "participating" to emptyList<String>()
            )

            db.collection("users").document(userId).set(userData).await()
            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // --- SIGN IN ---
    suspend fun signIn(email: String, password: String): Result<Unit> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // --- SIGN OUT ---
    fun signOut() = auth.signOut()

    // --- getUserData ---
    suspend fun getUserData(userId: String): User? {
        val snapshot = db.collection("users").document(userId).get().await()
        return snapshot.toObject(User::class.java)
    }



}
