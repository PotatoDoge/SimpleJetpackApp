package com.example.simplejetpackapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.simplejetpackapp.dashboard.DashboardScreen
import com.example.simplejetpackapp.login.LoginScreen
import com.example.simplejetpackapp.login.LoginViewModel
import com.example.simplejetpackapp.ui.theme.SimpleJetpackAppTheme

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class) // Needed for calculateWindowSizeClass
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimpleJetpackAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = androidx.compose.material3.MaterialTheme.colorScheme.background
                ) {
                    // Get the window size class
                    val windowSizeClass = calculateWindowSizeClass(this)

                    val loginViewModel: LoginViewModel = viewModel()
                    val isAuthenticated by loginViewModel.isAuthenticated.collectAsState()

                    if (isAuthenticated) {
                        // User is logged in, show the main app shell
                        DashboardScreen(
                            windowSizeClass = windowSizeClass.widthSizeClass,
                            // Pass the logout function as a lambda
                            onLogout = {
                                loginViewModel.logout()
                            }
                        )
                    } else {
                        // User is not logged in, show the login screen
                        LoginScreen(viewModel = loginViewModel)
                    }
                }
            }
        }
    }
}