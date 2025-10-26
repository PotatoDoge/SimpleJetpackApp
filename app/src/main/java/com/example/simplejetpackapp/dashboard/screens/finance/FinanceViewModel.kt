package com.example.simplejetpackapp.dashboard.screens.finance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Date
import java.util.Locale

// Data class to represent a single transaction
data class Transaction(
    val id: String,
    val title: String,
    val amount: Double,
    val date: Date,
    val type: TransactionType
)

enum class TransactionType {
    INCOME, EXPENSE
}

// UI state to hold the list of transactions
data class FinanceUiState(
    val transactions: List<Transaction> = emptyList(),
    val isLoading: Boolean = true
)

class FinanceViewModel : ViewModel() {

    // --- STATE FOR THE LIST ---
    private val _uiState = MutableStateFlow(FinanceUiState())
    val uiState: StateFlow<FinanceUiState> = _uiState.asStateFlow()

    // --- EVENT FOR NAVIGATION ---
    private val _navigateToAddTransaction = MutableSharedFlow<Unit>()
    val navigateToAddTransaction = _navigateToAddTransaction.asSharedFlow()

    init {
        // Load the (simulated) transactions when the ViewModel is created
        loadTransactions()
    }

    private fun loadTransactions() {
        viewModelScope.launch {
            // Simulate a network delay
            delay(1000)
            _uiState.value = FinanceUiState(
                isLoading = false,
                transactions = listOf(
                    Transaction("1", "Coffee Shop", -5.75, Date(), TransactionType.EXPENSE),
                    Transaction("2", "Paycheck", 2500.00, Date(), TransactionType.INCOME),
                    Transaction("3", "Groceries", -120.50, Date(), TransactionType.EXPENSE),
                    Transaction("4", "Electric Bill", -75.20, Date(), TransactionType.EXPENSE),
                    Transaction("5", "Freelance Gig", 300.00, Date(), TransactionType.INCOME)
                )
            )
        }
    }

    /**
     * Called when the user clicks the "+" FAB.
     * Fires a one-time event to trigger navigation.
     */
    fun onAddTransactionClicked() {
        viewModelScope.launch {
            _navigateToAddTransaction.emit(Unit)
        }
    }
}

// Helper function to format currency
fun formatCurrency(amount: Double): String {
    val format = NumberFormat.getCurrencyInstance(Locale.getDefault())
    return format.format(amount)
}

