package com.pocketpilot.pocketpilot.ui.expense

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.runtime.* // This fixes the 'getValue' delegate error
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pocketpilot.pocketpilot.ui.PocketViewModel
import com.pocketpilot.pocketpilot.ui.theme.PocketBlue

@Composable
fun ExpensesListScreen(viewModel: PocketViewModel) {
    // Fixed: Using your 'expenses' StateFlow from PocketViewModel
    val expenseList by viewModel.expenses.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Expenses",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = { /* TODO: Date Filter */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Filter by Date: Select")
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 16.dp)
        ) {
            items(expenseList) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Using 'expense' property from your Expense entity
                        Text(text = "Expense Item", style = MaterialTheme.typography.bodyLarge)
                        Text(
                            text = "R${item.expense}",
                            color = PocketBlue,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }

        Button(
            onClick = { /* TODO: Upload Logic */ },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = PocketBlue)
        ) {
            Icon(Icons.Default.PhotoCamera, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Upload Image")
        }
    }
}
