package com.example.civicapp.viewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.civicapp.data.models.Mission
import com.example.civicapp.data.repository.MissionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MissionViewModel : ViewModel() {
    private val repo = MissionRepository()

    // State for missions list
    private val _missions = MutableStateFlow<List<Mission>>(emptyList())
    val missions: StateFlow<List<Mission>> = _missions.asStateFlow()

    // State for loading
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // State for errors
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // State for selected category
    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    // Load all missions
    fun loadMissions() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                val data = repo.getAllMissions()
                _missions.value = data
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Filter by category
    fun filterByCategory(category: String?) {
        _selectedCategory.value = category
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                if (category == null || category == "Tous") {
                    val data = repo.getAllMissions()
                    _missions.value = data
                } else {
                    val data = repo.getMissionsByCategory(category)
                    _missions.value = data
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Filter error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Search missions
    fun searchMissions(query: String) {
        if (query.isEmpty()) {
            loadMissions()
            return
        }

        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                val data = repo.searchMissions(query)
                _missions.value = data
            } catch (e: Exception) {
                _error.value = e.message ?: "Search error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Create new mission
    fun createMission(mission: Mission) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                val success = repo.createMission(mission)
                if (success) {
                    loadMissions() // Refresh list
                } else {
                    _error.value = "Failed to create mission"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Creation error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Join a mission
    fun joinMission(missionId: String, userId: String) {
        viewModelScope.launch {
            try {
                _error.value = null
                val success = repo.joinMission(missionId, userId)
                if (success) {
                    loadMissions() // Refresh to show updated participants
                } else {
                    _error.value = "Failed to join mission"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Join error"
            }
        }
    }
}