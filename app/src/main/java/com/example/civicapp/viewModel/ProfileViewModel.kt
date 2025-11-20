package com.example.civicapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.civicapp.data.models.User
import com.example.civicapp.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: UserRepository = UserRepository()
) : ViewModel() {

    private val _userState = MutableStateFlow<User?>(null)
    val userState: StateFlow<User?> = _userState

    private val _updateState = MutableStateFlow<Result<Unit>?>(null)
    val updateState: StateFlow<Result<Unit>?> = _updateState

    // Load current logged-in user's profile from Firestore
    fun loadCurrentUser() {
        viewModelScope.launch {
            _userState.value = userRepository.getCurrentUserProfile()
        }
    }

    /**
     * Update current user's profile in BOTH Auth (email/password/displayName)
     * and Firestore (name, email, bio).
     */
    fun updateProfile(
        name: String?,
        email: String?,
        password: String?,  // null or "" if not changing
        bio: String?
    ) {
        viewModelScope.launch {
            val result = userRepository.updateCurrentUserProfile(
                newName = name?.takeIf { it.isNotBlank() },
                newEmail = email?.takeIf { it.isNotBlank() },
                newPassword = password?.takeIf { it.isNotBlank() },
                newBio = bio
            )

            _updateState.value = result

            if (result.isSuccess) {
                // Reload user profile from Firestore to refresh UI
                _userState.value = userRepository.getCurrentUserProfile()
            }
        }
    }

    fun deleteAccountAndLogout() {
        viewModelScope.launch {
            val result = userRepository.deleteCurrentUserAndLogout()
            if (result.isSuccess) {
                _userState.value = null
            }
            // You can expose a separate deleteState if the UI needs to show errors
        }
    }
}
