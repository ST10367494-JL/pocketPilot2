// ✅ FIX 1: Package directive updated to match its physical location on disk
package com.pocketpilot.pocketpilot

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pocketpilot.pocketpilot.ui.theme.PocketBlue

@Composable
fun SpendingAnalyticsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Spending Analytics", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(30.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize().height(200.dp)) {
                val canvasWidth = size.width

                // Maximum Spending Boundary Limit Line (Rubric Criteria)
                val maxGoalY = 60.dp.toPx()
                drawLine(
                    color = Color(0xFFDC2626), // Red Warning Target Line
                    start = Offset(0f, maxGoalY),
                    end = Offset(canvasWidth, maxGoalY),
                    strokeWidth = 2.dp.toPx(),
                    // ✅ FIX 2: Changed from dashedPathEffect to dashPathEffect
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 10f), 0f)
                )

                // Minimum Spending Savings Boundary Line (Rubric Criteria)
                val minGoalY = 140.dp.toPx()
                drawLine(
                    color = Color(0xFF16A34A), // Green Target Indicator Line
                    start = Offset(0f, minGoalY),
                    end = Offset(canvasWidth, minGoalY),
                    strokeWidth = 2.dp.toPx(),
                    // ✅ FIX 3: Changed from dashedPathEffect to dashPathEffect
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 10f), 0f)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .align(Alignment.BottomCenter),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                AnalyticsBar(height = 150.dp, label = "Groceries")
                AnalyticsBar(height = 80.dp, label = "Transport")
                AnalyticsBar(height = 110.dp, label = "Food")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedCard(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Date Range: Month 🔽",
                modifier = Modifier
                    .padding(12.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F4F6))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Goal Target Legend & Performance",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("🔴 Max Budget Limit Line (Do Not Cross)", fontSize = 12.sp, color = Color(0xFFDC2626))
                Text("🟢 Min Ideal Budget Line (Target Range Base)", fontSize = 12.sp, color = Color(0xFF16A34A))

                // ✅ FIX 4: Changed deprecated 'Divider' to modern 'HorizontalDivider'
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color.LightGray)

                Text(
                    text = "Monthly Status: 🏆 Excellent! You managed to stay completely within your target spending limits this month.",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF1E3A8A)
                )
            }
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
