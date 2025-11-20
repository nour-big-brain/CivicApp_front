package com.example.civicapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.civicapp.data.repository.ParticipationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ParticipationViewModel(
    private val participationRepo: ParticipationRepository = ParticipationRepository()
) : ViewModel() {

    private val _operationState = MutableStateFlow<Result<Unit>?>(null)
    val operationState: StateFlow<Result<Unit>?> = _operationState

    fun joinMission(userId: String, missionId: String) {
        viewModelScope.launch {
            val success = participationRepo.joinMission(userId, missionId)
            _operationState.value = if (success) Result.success(Unit) else Result.failure(Exception("Failed to join mission"))
        }
    }

    fun leaveMission(userId: String, missionId: String) {
        viewModelScope.launch {
            val success = participationRepo.leaveMission(userId, missionId)
            _operationState.value = if (success) Result.success(Unit) else Result.failure(Exception("Failed to leave mission"))
        }
    }
}
