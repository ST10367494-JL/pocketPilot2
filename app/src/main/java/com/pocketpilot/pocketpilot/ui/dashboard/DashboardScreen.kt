package com.pocketpilot.pocketpilot.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Dashboard UI layout built directly from the project's user wireframe specifications.
 * Uses standard programmatic layout alignments (Android Developers, 2024).
 */
@Composable
fun DashboardScreen(
    totalSpent: String,
    remaining: String,
    progress: Float,
    onNavigateToAddExpense: () -> Unit,
    onNavigateToCategories: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top App Context Header Block
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Dashboard",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E3A8A) // PocketPilot UI Signature Theme Blue
            )
        }

        // Financial KPI Metrics Grid Card Container (As per wireframe template)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F4F6))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Monthly Budget:", fontSize = 16.sp, color = Color.Gray)
                    Text("R5000", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Spent:", fontSize = 16.sp, color = Color.Gray)
                    Text(totalSpent, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFDC2626))
                }
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Remaining:", fontSize = 16.sp, color = Color.Gray)
                    Text(remaining, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF16A34A))
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Linear Visual Budget Usage Matrix Progress Tracker
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(45.dp)
                .background(Color(0xFFE5E7EB), RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.CenterStart
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(fraction = progress.coerceIn(0f, 1f))
                    .background(Color(0xFF3B82F6), RoundedCornerShape(8.dp))
            )
            Text(
                text = "${(progress * 100).toInt()}% Used",
                color = if (progress > 0.5f) Color.White else Color.Black,
                modifier = Modifier.padding(start = 12.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Transaction Entry Action Control Button
        Button(
            onClick = onNavigateToAddExpense,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E3A8A)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("➕ Add Expense", fontSize = 16.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
