package com.example.civicapp.data.repository

import com.example.civicapp.data.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {

    private val users = db.collection("users")

    // --- HELPERS TO EXPOSE CURRENT USER INFO ---

    fun getCurrentUserId(): String? = auth.currentUser?.uid

    fun getCurrentUserEmail(): String? = auth.currentUser?.email

    suspend fun getCurrentUserProfile(): User? {
        val uid = getCurrentUserId() ?: return null
        return getUser(uid)
    }

    // --- GET USER DATA BY ID ---
    suspend fun getUser(userId: String): User? {
        return try {
            val snapshot = users.document(userId)
                .get()
                .await()
            snapshot.toObject(User::class.java)
        } catch (e: Exception) {
            null
        }
    }

    // --- UPDATE CURRENT USER PROFILE (Auth + Firestore) ---
    suspend fun updateCurrentUserProfile(
        newName: String?,
        newEmail: String?,
        newPassword: String?,
        newBio: String?
    ): Result<Unit> {
        return try {
            val currentUser = auth.currentUser
                ?: return Result.failure(Exception("Aucun utilisateur connecté"))

            val uid = currentUser.uid

            // 1) Update Auth fields
            if (!newEmail.isNullOrBlank() && newEmail != currentUser.email) {
                currentUser.updateEmail(newEmail).await()
            }
            if (!newPassword.isNullOrBlank()) {
                currentUser.updatePassword(newPassword).await()
            }
            if (!newName.isNullOrBlank()) {
                val profileUpdate = com.google.firebase.auth.UserProfileChangeRequest.Builder()
                    .setDisplayName(newName)
                    .build()
                currentUser.updateProfile(profileUpdate).await()
            }

            // 2) Update Firestore profile fields
            val updates = mutableMapOf<String, Any?>()
            newName?.let { updates["name"] = it }
            newEmail?.let { updates["email"] = it }
            newBio?.let { updates["bio"] = it }

            if (updates.isNotEmpty()) {
                users.document(uid)
                    .update(updates)
                    .await()
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // --- OPTIONAL: replace full user doc ---
    suspend fun setUser(userId: String, user: User): Boolean {
        return try {
            users.document(userId)
                .set(user)
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }

    // --- PARTICIPATION / STATS ---
    suspend fun addActiveMission(userId: String, missionId: String): Boolean {
        return try {
            users.document(userId)
                .update("activeMissions", FieldValue.arrayUnion(missionId))
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun removeActiveMission(userId: String, missionId: String): Boolean {
        return try {
            users.document(userId)
                .update("activeMissions", FieldValue.arrayRemove(missionId))
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun incrementPoints(userId: String, amount: Long = 1): Boolean {
        return try {
            users.document(userId)
                .update("points", FieldValue.increment(amount))
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }

    // --- SINGLE FUNCTION: delete Auth user + Firestore doc + logout ---
    suspend fun deleteCurrentUserAndLogout(): Result<Unit> {
        return try {
            val currentUser = auth.currentUser
                ?: return Result.failure(Exception("Aucun utilisateur connecté"))

            val uid = currentUser.uid

            // 1) Delete the Firebase Authentication user account
            currentUser.delete().await()

            // 2) Delete the Firestore profile document under 'users/{uid}'
            users.document(uid)
                .delete()
                .await()

            // 3) Sign out locally
            auth.signOut()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addMissionToParticipation(userId: String, missionId: String): Boolean {
        return try {
            users.document(userId)
                .update("activeMissions", FieldValue.arrayUnion(missionId))
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun removeMissionFromParticipation(userId: String, missionId: String): Boolean {
        return try {
            users.document(userId)
                .update("activeMissions", FieldValue.arrayRemove(missionId))
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }
}
