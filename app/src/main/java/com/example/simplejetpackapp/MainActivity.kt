package com.example.simplejetpackapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.simplejetpackapp.dashboard.DashboardScreen
import com.example.simplejetpackapp.login.LoginScreen
import com.example.simplejetpackapp.login.LoginViewModel
import com.example.simplejetpackapp.ui.theme.SimpleJetpackAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimpleJetpackAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = androidx.compose.material3.MaterialTheme.colorScheme.background
                ) {
                    val loginViewModel: LoginViewModel = viewModel()

                    val isAuthenticated by loginViewModel.isAuthenticated.collectAsState()

                    if (isAuthenticated) {
                        DashboardScreen()
                    } else {
                        LoginScreen(viewModel = loginViewModel)
                    }
                }
            }
        }
    }
}
