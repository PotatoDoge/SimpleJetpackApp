package com.example.simplejetpackapp.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Savings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.simplejetpackapp.dashboard.screens.home.HomeScreen
import com.example.simplejetpackapp.dashboard.screens.ProfileScreen
import com.example.simplejetpackapp.dashboard.screens.ReportsScreen
import com.example.simplejetpackapp.dashboard.screens.finance.FinanceScreen
import com.example.simplejetpackapp.dashboard.screens.finance.transaction.AddTransactionScreen

// Data class to represent our navigation items
private data class NavItem(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

// List of our 4 main destinations
private val navigationItems = listOf(
    NavItem(
        route = AppDestinations.HOME,
        label = "Home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    ),
    NavItem(
        route = AppDestinations.FINANCE,
        label = "Finance",
        selectedIcon = Icons.Filled.Savings,
        unselectedIcon = Icons.Outlined.Savings
    ),
    NavItem(
        route = AppDestinations.REPORTS,
        label = "Reports",
        selectedIcon = Icons.Filled.BarChart,
        unselectedIcon = Icons.Outlined.BarChart
    ),
    NavItem(
        route = AppDestinations.PROFILE,
        label = "Profile",
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person
    ),
)

/**
 * This is the main "shell" of the app after login.
 * It handles the responsive navigation (Bottom Bar vs. Rail) and
 * displays the correct sub-screen.
 */
@Composable
fun DashboardScreen(
    windowSizeClass: WindowWidthSizeClass,
    onLogout: () -> Unit,
    dashboardViewModel: DashboardViewModel = viewModel()
) {
    val selectedRoute by dashboardViewModel.selectedRoute.collectAsState()
    val isAddingTransaction by dashboardViewModel.isAddingTransaction.collectAsState()

    // Check if we should show the Navigation Rail (for Medium/Expanded screens)
    val showNavRail = windowSizeClass == WindowWidthSizeClass.Medium ||
            windowSizeClass == WindowWidthSizeClass.Expanded

    // This Box will contain our main UI and the animated overlay
    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            // We only show the bottom bar if we are NOT showing the nav rail
            bottomBar = {
                if (!showNavRail) {
                    AppBottomNavigationBar(
                        items = navigationItems,
                        selectedRoute = selectedRoute,
                        onItemClick = { route ->
                            dashboardViewModel.onNavigationItemSelected(route)
                        }
                    )
                }
            }
        ) { innerPadding ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding) // Apply padding from the Scaffold
            ) {
                // Show the Nav Rail if we have space
                if (showNavRail) {
                    AppNavigationRail(
                        items = navigationItems,
                        selectedRoute = selectedRoute,
                        onItemClick = { route ->
                            dashboardViewModel.onNavigationItemSelected(route)
                        }
                    )
                }

                Box(modifier = Modifier.fillMaxSize()) {
                    when (selectedRoute) {
                        AppDestinations.HOME -> HomeScreen()
                        AppDestinations.FINANCE -> FinanceScreen(
                            onNavigateToAddTransaction = {
                                dashboardViewModel.onShowAddTransaction()
                            }
                        )
                        AppDestinations.REPORTS -> ReportsScreen()
                        AppDestinations.PROFILE -> ProfileScreen(
                            // The onLogout lambda is passed down to the ProfileScreen
                            onLogoutClicked = onLogout
                        )
                    }
                }
            }
        }

        // --- ADD TRANSACTION OVERLAY ---
        // This composable is drawn *outside* the Scaffold, so it animates
        // over the top of the entire screen, including the bottom nav bar.
        AnimatedVisibility(
            visible = isAddingTransaction,
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it })
        ) {
            AddTransactionScreen(
                onDismiss = {
                    dashboardViewModel.onDismissAddTransaction()
                }
            )
        }
    }
}

/**
 * The Bottom Navigation Bar for compact screens (phones).
 */
@Composable
private fun AppBottomNavigationBar(
    items: List<NavItem>,
    selectedRoute: String,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    ) {
        items.forEach { item ->
            val isSelected = item.route == selectedRoute
            NavigationBarItem(
                selected = isSelected,
                onClick = { onItemClick(item.route) },
                label = { Text(item.label) },
                icon = {
                    Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.label
                    )
                }
            )
        }
    }
}

/**
 * The Navigation Rail for medium/expanded screens (tablets).
 */
@Composable
private fun AppNavigationRail(
    items: List<NavItem>,
    selectedRoute: String,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationRail(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    ) {
        items.forEach { item ->
            val isSelected = item.route == selectedRoute
            NavigationRailItem(
                selected = isSelected,
                onClick = { onItemClick(item.route) },
                label = { Text(item.label) },
                icon = {
                    Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.label
                    )
                }
            )
        }
    }
}

