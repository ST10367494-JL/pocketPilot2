package com.pocketpilot.pocketpilot.ui.analytics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.pocketpilot.pocketpilot.ui.theme.PocketBlue

@Composable
fun SpendingAnalyticsScreen() {
    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Spending Analytics", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(50.dp))

        // Simple Bar Chart
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            AnalyticsBar(height = 150.dp, label = "Groceries")
            AnalyticsBar(height = 80.dp, label = "Transport")
            AnalyticsBar(height = 110.dp, label = "Food")
        }

        Spacer(modifier = Modifier.height(40.dp))

        OutlinedCard(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Date Range: Month",
                modifier = Modifier.padding(16.dp).align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun AnalyticsBar(height: Dp, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .height(height)
                .width(40.dp)
                .background(PocketBlue, shape = MaterialTheme.shapes.small)
        )
        Text(text = label, style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(top = 8.dp))
    }
}
