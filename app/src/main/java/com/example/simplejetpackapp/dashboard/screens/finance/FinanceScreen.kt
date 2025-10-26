package com.example.simplejetpackapp.dashboard.screens.finance

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.simplejetpackapp.ui.theme.SimpleJetpackAppTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun FinanceScreen(
    onNavigateToAddTransaction: () -> Unit,
    financeViewModel: FinanceViewModel = viewModel()
) {
    val uiState by financeViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        financeViewModel.navigateToAddTransaction.collect {
            onNavigateToAddTransaction()
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    financeViewModel.onAddTransactionClicked()
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add transaction")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Title
            Text(
                text = "Transactions",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(16.dp)
            )

            if (uiState.isLoading) {
                // Show a loading spinner while loading
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (uiState.transactions.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No transactions yet.")
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(uiState.transactions, key = { it.id }) { transaction ->
                        TransactionItem(transaction = transaction)
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                    }
                }
            }
        }
    }
}

/**
 * A single row composable to display one transaction.
 */
@Composable
fun TransactionItem(transaction: Transaction) {
    // Define colors for income/expense
    val amountColor = if (transaction.type == TransactionType.INCOME) {
        Color(0xFF00897B) // Muted Green
    } else {
        MaterialTheme.colorScheme.error // Muted Red
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Left side: Title and Date
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = transaction.title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = transaction.date.format(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        // Right side: Amount
        Text(
            text = formatCurrency(transaction.amount),
            style = MaterialTheme.typography.bodyLarge,
            color = amountColor,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.End
        )
    }
}

// Simple helper function to format a Date
private fun Date.format(): String {
    val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return formatter.format(this)
}

// --- PREVIEWS ---

@Preview(showBackground = true)
@Composable
private fun FinanceScreenPreview() {
    // --- FIX WAS HERE ---
    SimpleJetpackAppTheme {
        FinanceScreen(onNavigateToAddTransaction = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun TransactionItemPreview() {
    val testTransaction = Transaction(
        id = "1",
        title = "Coffee Shop",
        amount = -5.75,
        date = Date(),
        type = TransactionType.EXPENSE
    )
    // --- FIX WAS HERE ---
    SimpleJetpackAppTheme {
        TransactionItem(transaction = testTransaction)
    }
}

@Preview(showBackground = true)
@Composable
private fun TransactionItemIncomePreview() {
    val testTransaction = Transaction(
        id = "2",
        title = "Paycheck",
        amount = 2500.00,
        date = Date(),
        type = TransactionType.INCOME
    )
    // --- FIX WAS HERE ---
    SimpleJetpackAppTheme {
        TransactionItem(transaction = testTransaction)
    }
}

