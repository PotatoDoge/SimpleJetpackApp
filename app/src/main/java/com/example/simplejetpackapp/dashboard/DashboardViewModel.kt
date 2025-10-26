package com.example.simplejetpackapp.dashboard

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Defines the navigation routes for the main app screens.
 */
object AppDestinations {
    const val HOME = "home"
    const val FINANCE = "finance"
    const val REPORTS = "reports"
    const val PROFILE = "profile"
}

/**
 * This is the "brain" for the main Dashboard.
 * It's responsible for managing the navigation state (which tab is selected)
 * and the state of any global overlays (like the Add Transaction screen).
 */
class DashboardViewModel : ViewModel() {

    // Internal state for the selected navigation route
    private val _selectedRoute = MutableStateFlow(AppDestinations.HOME)
    // Public, read-only state flow for the UI to observe
    val selectedRoute: StateFlow<String> = _selectedRoute.asStateFlow()

    // Internal state for showing the "Add Transaction" overlay
    private val _isAddingTransaction = MutableStateFlow(false)
    // Public, read-only state flow for the UI to observe
    val isAddingTransaction: StateFlow<Boolean> = _isAddingTransaction.asStateFlow()

    /**
     * Called by the UI when a navigation item (bottom bar or rail) is clicked.
     */
    fun onNavigationItemSelected(route: String) {
        _selectedRoute.value = route
    }

    /**
     * Called by the FinanceScreen to show the "Add Transaction" overlay.
     */
    fun onShowAddTransaction() {
        _isAddingTransaction.value = true
    }

    /**
     * Called by the AddTransactionScreen to dismiss itself.
     */
    fun onDismissAddTransaction() {
        _isAddingTransaction.value = false
    }
}