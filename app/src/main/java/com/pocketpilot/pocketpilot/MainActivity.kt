package com.pocketpilot.pocketpilot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.pocketpilot.pocketpilot.ui.theme.PocketPilotTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.unit.dp
import com.pocketpilot.pocketpilot.ui.theme.PocketBlue
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PocketPilotTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Change this to your new DashboardScreen
                    DashboardScreen(innerPadding)
                }
            }
        }
    }
}


@Composable
fun BudgetCircle(percentage: Float, remainingAmount: String) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(200.dp)) {
        CircularProgressIndicator(
            progress = percentage,
            modifier = Modifier.fillMaxSize(),
            color = if (percentage > 0.9f) Color.Red else PocketBlue, // Visual alert if near limit
            strokeWidth = 12.dp
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "${(percentage * 100).toInt()}% Used", style = MaterialTheme.typography.headlineSmall)
            Text(text = "Remaining: $remainingAmount", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun DashboardScreen(innerPadding: PaddingValues) {
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Dashboard", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(40.dp))

        // Your Circular Progress Component
        BudgetCircle(percentage = 0.45f, remainingAmount = "R2800")

        Spacer(modifier = Modifier.height(40.dp))

        // Budget Summary Card
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Monthly Budget: R5000", style = MaterialTheme.typography.titleMedium)
                Text("Total Spent: R2200", color = PocketBlue)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { /* Navigate to Add Expense */ },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = PocketBlue)
        ) {
            Text("+ Add Expense")
        }
    }
}
