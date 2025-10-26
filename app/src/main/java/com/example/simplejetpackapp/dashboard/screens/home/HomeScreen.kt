package com.example.simplejetpackapp.dashboard.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday

import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.simplejetpackapp.dashboard.HomeScreenUiState
import com.example.simplejetpackapp.dashboard.HomeViewModel
import com.example.simplejetpackapp.dashboard.screens.finance.formatCurrency
import com.example.simplejetpackapp.ui.theme.SimpleJetpackAppTheme

/**
 * This is the main Home Screen composable.
 * It observes the ViewModel and delegates rendering to smaller components.
 */
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = viewModel()
) {
    val uiState by homeViewModel.uiState.collectAsState()

    // The main layout is a scrollable column
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp), // Content padding
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 64.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            // --- Section 1: Overview Card ---
            OverviewCard(
                income = uiState.income,
                expense = uiState.expense,
                dateRange = uiState.dateRangeText,
                onDateRangeClick = { homeViewModel.onDateRangeClicked() }
            )

            // (Other sections have been removed)
        }
    }
}

/**
 * Section 1: Displays Income, Expense, and Date Range. (IN A CARD)
 */
@Composable
private fun OverviewCard(
    income: Double,
    expense: Double,
    dateRange: String,
    onDateRangeClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Title
            Text("Overview", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(16.dp))

            // Income / Expense Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                InfoColumn(
                    title = "Income",
                    amount = income,
                    color = Color(0xFF00897B) // Muted Green
                )
                InfoColumn(
                    title = "Expense",
                    amount = expense,
                    color = Color(0xFFE53935) // Muted Red
                )
            }
            Spacer(Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(Modifier.height(16.dp))

            // Date Range
            AssistChip(
                onClick = onDateRangeClick,
                label = { Text(dateRange) },
                leadingIcon = {
                    Icon(
                        Icons.Default.CalendarToday,
                        contentDescription = "Select Date Range"
                    )
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

// --- SUB-COMPONENTS ---

/**
 * A small column for displaying "Income" or "Expense".
 */
@Composable
private fun InfoColumn(title: String, amount: Double, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = formatCurrency(amount),
            style = MaterialTheme.typography.headlineSmall,
            color = color,
            fontWeight = FontWeight.Bold
        )
    }
}


// --- PREVIEW ---

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    SimpleJetpackAppTheme {
        // We can't preview the ViewModel easily, so we make a fake state
        val fakeState = HomeScreenUiState(
            income = 5230.50,
            expense = 1845.20,
            isLoading = false
        )

        // Pass the fake state to composables
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OverviewCard(
                income = fakeState.income,
                expense = fakeState.expense,
                dateRange = fakeState.dateRangeText,
                onDateRangeClick = {}
            )
        }
    }
}

