package com.example.civicapp.data.repository

import com.example.civicapp.data.models.Mission
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

import kotlinx.coroutines.tasks.await

class MissionRepository {
    private val db = Firebase.firestore

    suspend fun getAllMissions(): List<Mission> {
        return try {
            db.collection("missions")
                .get()
                .await()
                .toObjects(Mission::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getMissionsByCategory(category: String): List<Mission> {
        return try {
            db.collection("missions")
                .whereEqualTo("category", category)
                .get()
                .await()
                .toObjects(Mission::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun searchMissions(query: String): List<Mission> {
        return try {
            // Simple client-side search (you can use server-side filters)
            val allMissions = getAllMissions()
            allMissions.filter { mission ->
                mission.title.contains(query, ignoreCase = true) ||
                        mission.description.contains(query, ignoreCase = true)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun createMission(mission: Mission): Boolean {
        return try {
            db.collection("missions")
                .add(mission)
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun joinMission(missionId: String, userId: String): Boolean {
        return try {
            db.collection("missions")
                .document(missionId)
                .update(
                    "participants",
                    com.google.firebase.firestore.FieldValue.arrayUnion(userId)
                )
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }
}