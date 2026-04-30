package com.pocketpilot.pocketpilot.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.* // Fixed to Material 3
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pocketpilot.pocketpilot.ui.theme.PocketBlue

@Composable
fun ProfileScreen(onLogout: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Default.Person,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = PocketBlue
        )

        Text(text = "Username: Lihle", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(40.dp))

        Text(text = "Achievements", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(20.dp))

        AchievementItem("Budget Saver")
        AchievementItem("Expense Logger")
        AchievementItem("Smart Spender")

        Spacer(modifier = Modifier.weight(1f))

        TextButton(onClick = onLogout) {
            Text("Log Out", color = Color.Red)
        }
    }
}

@Composable
fun AchievementItem(title: String) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Badge, contentDescription = null, tint = PocketBlue)
            Spacer(Modifier.width(16.dp))
            Text(text = title)
        }
    }
}
