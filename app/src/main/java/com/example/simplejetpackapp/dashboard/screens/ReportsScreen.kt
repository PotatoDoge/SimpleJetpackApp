package com.example.simplejetpackapp.dashboard.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.simplejetpackapp.ui.theme.SimpleJetpackAppTheme

@Composable
fun ReportsScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.BarChart,
            contentDescription = "Reports",
            modifier = Modifier.fillMaxSize(0.3f),
            tint = MaterialTheme.colorScheme.secondary
        )
        Text(
            text = "Reports Screen",
            style = MaterialTheme.typography.headlineSmall
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ReportsScreenPreview() {
    SimpleJetpackAppTheme {
        ReportsScreen()
    }
}
