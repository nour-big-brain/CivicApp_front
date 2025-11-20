package com.example.civicapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.civicapp.data.models.User
import com.example.civicapp.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    // ============ STATE VARIABLES ============

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    // One-time events
    private val _loginEvents = MutableSharedFlow<Result<Unit>>()
    val loginEvents: SharedFlow<Result<Unit>> = _loginEvents.asSharedFlow()

    private val _signupEvents = MutableSharedFlow<Result<Unit>>()
    val signupEvents: SharedFlow<Result<Unit>> = _signupEvents.asSharedFlow()

    init {
        checkAuthStatus()
    }

    // ============ CHECK AUTH STATUS ============

    private fun checkAuthStatus() {
        viewModelScope.launch {
            try {
                val loggedIn = authRepository.isUserLoggedIn()
                _isLoggedIn.value = loggedIn

                if (loggedIn) {
                    val userId = authRepository.getCurrentUserId()
                    _user.value = userId?.let { id ->
                        authRepository.getUserData(id)
                    }
                } else {
                    _user.value = null
                }
            } catch (e: Exception) {
                _isLoggedIn.value = false
                _user.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }

    // ============ LOGIN FUNCTION ============

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val result = try {
                if (email.isEmpty() || password.isEmpty()) {
                    throw Exception("Email and password cannot be empty")
                }

                val signInResult = authRepository.signIn(email, password)

                if (signInResult.isSuccess) {
                    _isLoggedIn.value = true

                    val userId = authRepository.getCurrentUserId()
                    _user.value = userId?.let { id ->
                        authRepository.getUserData(id)
                    }
                    Result.success(Unit)
                } else {
                    signInResult
                }
            } catch (e: Exception) {
                Result.failure(e)
            }

            _loginEvents.emit(result)
        }
    }

    // ============ SIGNUP FUNCTION ============

    fun signup(name: String, email: String, password: String) {
        viewModelScope.launch {
            val result = try {
                if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    throw Exception("All fields are required")
                }
                if (password.length < 6) {
                    throw Exception("Password must be at least 6 characters")
                }
                if (!email.contains("@")) {
                    throw Exception("Invalid email format")
                }

                val signUpResult = authRepository.signUp(name, email, password)

                if (signUpResult.isSuccess) {
                    _isLoggedIn.value = true

                    val userId = authRepository.getCurrentUserId()
                    _user.value = userId?.let { id ->
                        authRepository.getUserData(id)
                    }
                    Result.success(Unit)
                } else {
                    signUpResult
                }
            } catch (e: Exception) {
                Result.failure(e)
            }

            _signupEvents.emit(result)
        }
    }

    // ============ LOGOUT FUNCTION ============

    fun logout() {
        authRepository.signOut()
        _isLoggedIn.value = false
        _user.value = null
    }

    // ============ HELPER FUNCTIONS ============

    fun getCurrentUserId(): String? = authRepository.getCurrentUserId()

    fun getCurrentUserName(): String? = _user.value?.name

    fun updateUser(name: String, email: String) {
        _user.value = _user.value?.copy(name = name, email = email)
        // If you also want this in Firestore, call a repository method to update the document.
    }
}



//TEST THIS OUT LATTER
/*
package com.example.civicapp.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import com.example.civicapp.data.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AuthViewModel : ViewModel() {
    // ============ STATE VARIABLES ============
    // These are "observed" by the UI - when they change, UI updates

    // Is user currently logged in?
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    // Is app still loading/checking auth?
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Current logged in user info
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    // Result of login attempt
    private val _loginState = MutableStateFlow<Result<Unit>?>(null)
    val loginState: StateFlow<Result<Unit>?> = _loginState.asStateFlow()

    // Result of signup attempt
    private val _signupState = MutableStateFlow<Result<Unit>?>(null)
    val signupState: StateFlow<Result<Unit>?> = _signupState.asStateFlow()

    // Firebase instances
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    init {
        // When ViewModel is created, check if user is already logged in
        checkAuthStatus()
    }

    // ============ CHECK AUTH STATUS ============
    // This runs when app opens to see if user is already logged in
    private fun checkAuthStatus() {
        viewModelScope.launch {
            try {
                // Simulate checking (replace with actual logic)
                delay(1500)

                // Check if Firebase has a current user
                val currentUser = auth.currentUser

                if (currentUser != null) {
                    // User is logged in - fetch their info
                    _user.value = User(
                        id = currentUser.uid,
                        name = currentUser.displayName ?: "User",
                        email = currentUser.email ?: ""
                    )
                    _isLoggedIn.value = true
                    Log.d("AuthViewModel", "User already logged in: ${currentUser.email}")
                } else {
                    // User is not logged in
                    _isLoggedIn.value = false
                    Log.d("AuthViewModel", "No user logged in")
                }
            } catch (e: Exception) {
                // Something went wrong
                Log.e("AuthViewModel", "Error checking auth: ${e.message}")
                _isLoggedIn.value = false
            } finally {
                // Finished loading, hide splash
                _isLoading.value = false
            }
        }
    }

    // ============ LOGIN FUNCTION ============
    // Called when user clicks "Login" button on login screen
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = try {
                // Check if inputs are empty
                if (email.isEmpty() || password.isEmpty()) {
                    throw Exception("Email and password cannot be empty")
                }

                // Try to sign in with Firebase
                val result = auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Login successful
                            val firebaseUser = task.result?.user
                            _user.value = User(
                                id = firebaseUser?.uid ?: "",
                                name = firebaseUser?.displayName ?: "User",
                                email = firebaseUser?.email ?: ""
                            )
                            _isLoggedIn.value = true
                            Log.d("AuthViewModel", "Login successful: ${email}")
                        } else {
                            // Login failed
                            throw Exception(task.exception?.message ?: "Login failed")
                        }
                    }

                Result.success(Unit)
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Login error: ${e.message}")
                Result.failure(e)
            }
        }
    }

    // ============ SIGNUP FUNCTION ============
    // Called when user clicks "Sign Up" button on register screen
    fun signup(name: String, email: String, password: String) {
        viewModelScope.launch {
            _signupState.value = try {
                // Check if inputs are empty
                if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
                    throw Exception("All fields are required")
                }

                // Check if password is strong enough
                if (password.length < 6) {
                    throw Exception("Password must be at least 6 characters")
                }

                // Try to create account with Firebase
                val result = auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Account created successfully
                            val firebaseUser = task.result?.user

                            // Update profile with name
                            firebaseUser?.updateProfile(
                                com.google.firebase.auth.UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .build()
                            )

                            // Save user info to Firestore
                            firestore.collection("users").document(firebaseUser?.uid ?: "")
                                .set(mapOf(
                                    "name" to name,
                                    "email" to email,
                                    "createdAt" to com.google.firebase.Timestamp.now()
                                ))

                            _user.value = User(
                                id = firebaseUser?.uid ?: "",
                                name = name,
                                email = email
                            )
                            _isLoggedIn.value = true
                            Log.d("AuthViewModel", "Signup successful: ${email}")
                        } else {
                            throw Exception(task.exception?.message ?: "Signup failed")
                        }
                    }

                Result.success(Unit)
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Signup error: ${e.message}")
                Result.failure(e)
            }
        }
    }

    // ============ LOGOUT FUNCTION ============
    // Called when user clicks "Logout" button on profile screen
    fun logout() {
        try {
            auth.signOut()
            _isLoggedIn.value = false
            _user.value = null
            _loginState.value = null
            _signupState.value = null
            Log.d("AuthViewModel", "User logged out")
        } catch (e: Exception) {
            Log.e("AuthViewModel", "Logout error: ${e.message}")
        }
    }

    // ============ HELPER FUNCTIONS ============

    fun getCurrentUserId(): String? = _user.value?.id

    fun getCurrentUserName(): String? = _user.value?.name

    fun updateUser(name: String, email: String) {
        _user.value = _user.value?.copy(name = name, email = email)
    }
}

// ============ USER DATA MODEL ============
// This is in data/models/User.kt
/*
package com.example.civicapp.data.models

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = ""
)
*/
 */