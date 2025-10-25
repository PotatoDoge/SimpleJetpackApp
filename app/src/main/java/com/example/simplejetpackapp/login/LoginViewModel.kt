package com.example.simplejetpackapp.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    // Input state
    var emailInput by mutableStateOf("")
    var passwordInput by mutableStateOf("")

    // UI State and Feedback
    var isLoading by mutableStateOf(false)
    var loginError by mutableStateOf<String?>(null)

    // Auth State: Publicly exposed as an immutable StateFlow
    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated.asStateFlow()

    // Validation
    private val isEmailValid: Boolean
        get() = android.util.Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()
    private val isPasswordValid: Boolean
        get() = passwordInput.length >= 6

    // Action function (Launches a Coroutine)
    fun attemptLogin() {
        loginError = null
        if (!isEmailValid || !isPasswordValid) {
            loginError = if (!isEmailValid) "Please enter a valid email address." else "Password must be at least 6 characters."
            return
        }

        isLoading = true

        // Launch the simulated network call in the ViewModel's Coroutine scope
        viewModelScope.launch {
            delay(1500L) // Simulate network latency (1.5 seconds)

            if (emailInput == "test@app.com" && passwordInput == "password") {
                // SUCCESS: Update the mutable backing flow
                _isAuthenticated.value = true
            } else {
                // FAILURE
                loginError = "Invalid email or password. Try 'test@app.com' / 'password'."
            }
            isLoading = false
        }
    }
}