package com.example.civicapp.data.repository

import com.example.civicapp.data.models.Mission
import kotlinx.coroutines.tasks.await
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class MissionRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    private val missions = db.collection("missions")

    // --- CREATE MISSION ---
    suspend fun createMission(mission: Mission): Boolean {
        return try {
            missions.add(mission).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    // --- UPDATE MISSION ---
    suspend fun updateMission(missionId: String, updates: Map<String, Any>): Boolean {
        return try {
            missions.document(missionId)
                .update(updates)
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }

    // --- DELETE MISSION ---
    suspend fun deleteMission(missionId: String): Boolean {
        return try {
            missions.document(missionId)
                .delete()
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }

    // --- GET ALL MISSIONS ---
    suspend fun getAllMissions(): List<Mission> {
        return try {
            missions.get().await().toObjects(Mission::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    // --- GET MISSION DETAILS ---
    suspend fun getMissionById(missionId: String): Mission? {
        return try {
            missions.document(missionId).get().await().toObject(Mission::class.java)
        } catch (e: Exception) {
            null
        }
    }

    // --- JOIN MISSION ---
    suspend fun joinMission(missionId: String, userId: String): Boolean {
        return try {
            missions.document(missionId)
                .update("participants", FieldValue.arrayUnion(userId))
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }

    // --- LEAVE MISSION ---
    suspend fun leaveMission(missionId: String, userId: String): Boolean {
        return try {
            missions.document(missionId)
                .update("participants", FieldValue.arrayRemove(userId))
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }

    // --- UPDATE MISSION STATUS (only creator allowed) ---
    suspend fun updateMissionStatus(
        missionId: String,
        creatorId: String,
        newStatus: String
    ): Boolean {
        return try {
            val mission = missions.document(missionId).get().await()

            if (mission.getString("creatorId") != creatorId) return false

            missions.document(missionId)
                .update("status", newStatus)
                .await()

            true
        } catch (e: Exception) {
            false
        }
    }

    // --- SEARCH MISSIONS ---
    suspend fun searchMissions(query: String): List<Mission> {
        return try {
            val allMissions = getAllMissions()
            allMissions.filter { mission ->
                mission.title.contains(query, ignoreCase = true) ||
                        mission.description.contains(query, ignoreCase = true)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    // --- FILTER MISSIONS BY CATEGORY ---
    suspend fun getMissionsByCategory(category: String): List<Mission> {
        return try {
            missions
                .whereEqualTo("category", category)
                .get()
                .await()
                .toObjects(Mission::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }
}


/*
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

 */