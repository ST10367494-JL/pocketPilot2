package com.pocketpilot.pocketpilot.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pocketpilot.pocketpilot.ui.PocketViewModel
import com.pocketpilot.pocketpilot.ui.theme.PocketBlue

/**
 * Profile and Gamified Achievement matrix layout built directly from your wireframe.
 * Integrates dynamic conditional rendering rules outlined by Android Developers (2024).
 */
@Composable
fun ProfileScreen(
    viewModel: PocketViewModel,
    onLogout: () -> Unit
) {
    // Dynamic integration mapping: reads live expenses from your Firebase table to unlock achievements
    val expenses by viewModel.expenses.collectAsState(initial = emptyList())

    // Core Game Mechanics Logic based on live user action data metrics (Firebase, 2026)
    val totalExpensesLogged = expenses.size
    val totalSpentAmount = expenses.sumOf { it.expense }.toFloat()

    // Dynamic unlocking conditions evaluating state triggers
    val isExpenseLoggerUnlocked = totalExpensesLogged >= 1   // Unlocks on logging first transaction
    val isSmartSpenderUnlocked = totalExpensesLogged >= 5    // Unlocks on logging five transactions
    val isBudgetSaverUnlocked = totalSpentAmount in 1f..4999f // Unlocks if spending stays under the R5000 limit

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = PocketBlue
        )

        Text(
            text = "Username: Lihle",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(30.dp))

        Text(text = "Achievements Platform", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        // Render individual status modules connected directly to runtime triggers (Android Developers, 2024)
        AchievementItem(
            title = "Budget Saver",
            description = "Keep total monthly expenses under the R5000 budget cap limit.",
            isUnlocked = isBudgetSaverUnlocked
        )

        AchievementItem(
            title = "Expense Logger",
            description = "Successfully log your first financial expense transaction node to Firebase.",
            isUnlocked = isExpenseLoggerUnlocked
        )

        AchievementItem(
            title = "Smart Spender",
            description = "Build a tracking streak by registering 5 distinct balance items.",
            isUnlocked = isSmartSpenderUnlocked
        )

        Spacer(modifier = Modifier.weight(1f))

        TextButton(onClick = onLogout) {
            Text("Log Out Screen View", color = Color.Red, fontWeight = FontWeight.Bold)
        }
    }
}

/**
 * Achievement card component designed to visually differentiate locked and unlocked states.
 * Employs adaptive component layouts specified by Android Developers (2024).
 */
@Composable
fun AchievementItem(title: String, description: String, isUnlocked: Boolean) {
    // Adaptive color styling parameters mapping state changes to theme assets
    val cardBackground = if (isUnlocked) Color(0xFFFEF3C7) else MaterialTheme.colorScheme.surfaceVariant // Soft Amber vs Default Gray
    val badgeIconTint = if (isUnlocked) Color(0xFFD97706) else Color.Gray // Golden Amber vs Locked Gray

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = cardBackground)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Adaptive conditional status rendering block (Android Developers, 2024)
            Icon(
                imageVector = if (isUnlocked) Icons.Default.Badge else Icons.Default.Lock,
                contentDescription = if (isUnlocked) "Unlocked" else "Locked",
                tint = badgeIconTint
            )

            Spacer(Modifier.width(16.dp))

            Column {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    color = if (isUnlocked) Color(0xFF92400E) else Color.Unspecified
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.DarkGray
                )
            }
        }
    }
}
