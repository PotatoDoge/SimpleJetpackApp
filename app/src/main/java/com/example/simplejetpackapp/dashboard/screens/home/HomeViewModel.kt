package com.example.simplejetpackapp.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// --- DATA MODELS ---

/**
 * This holds ALL the data for the home screen.
 * The UI will just observe this one object.
 */
data class HomeScreenUiState(
    val income: Double = 0.0,
    val expense: Double = 0.0,
    val startDate: LocalDate = LocalDate.now().withDayOfMonth(1),
    val endDate: LocalDate = LocalDate.now(),
    val isLoading: Boolean = true
) {
    // Helper property to get a formatted date range string
    val dateRangeText: String
        get() {
            val formatter = DateTimeFormatter.ofPattern("MMM d")
            return "${startDate.format(formatter)} - ${endDate.format(formatter)}"
        }
}


// --- VIEW MODEL ---

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HomeScreenUiState())
    val uiState: StateFlow<HomeScreenUiState> = _uiState.asStateFlow()

    init {
        // When the ViewModel is created, load the data
        loadData()
    }

    /**
     * Simulates fetching all the dashboard data from a server or database.
     */
    private fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // Simulate a network delay
            delay(1500)

            _uiState.update {
                it.copy(
                    income = 5230.50,
                    expense = 1845.20,
                    isLoading = false
                )
            }
        }
    }

    /**
     * Called by the UI when the date range chip is clicked.
     * In a real app, this would open a Date Range Picker dialog.
     */
    fun onDateRangeClicked() {
        println("Date range chip clicked! Would open a date picker.")
    }
}

