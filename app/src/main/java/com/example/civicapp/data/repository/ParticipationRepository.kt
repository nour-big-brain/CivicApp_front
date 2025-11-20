package com.example.civicapp.data.repository

class ParticipationRepository(
    private val userRepo: UserRepository = UserRepository(),
    private val missionRepo: MissionRepository = MissionRepository()
) {

    // JOIN A MISSION
    suspend fun joinMission(userId: String, missionId: String): Boolean {
        val missionUpdated = missionRepo.joinMission(missionId, userId)
        val userUpdated = userRepo.addMissionToParticipation(userId, missionId)
        return missionUpdated && userUpdated
    }

    // LEAVE A MISSION
    suspend fun leaveMission(userId: String, missionId: String): Boolean {
        val missionUpdated = missionRepo.leaveMission(missionId, userId)
        val userUpdated = userRepo.removeMissionFromParticipation(userId, missionId)
        return missionUpdated && userUpdated
    }
}