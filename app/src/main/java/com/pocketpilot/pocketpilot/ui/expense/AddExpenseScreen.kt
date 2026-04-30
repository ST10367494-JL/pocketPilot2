package com.pocketpilot.pocketpilot.ui.expense

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pocketpilot.pocketpilot.ui.PocketViewModel
import com.pocketpilot.pocketpilot.ui.theme.PocketBlue
import com.pocketpilot.pocketpilot.data.entities.Expense
import java.util.Date

@Composable
fun AddExpenseScreen(
    viewModel: PocketViewModel,
    onBack: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Text(
            text = "Add Expense",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Amount") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                val expenseValue = amount.toDoubleOrNull()

                if (title.isNotBlank() && expenseValue != null) {

                    val expense = Expense(
                        description = title,
                        expense = expenseValue,
                        dateAdded = Date()
                    )

                    viewModel.addExpense(expense)
                    onBack()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = PocketBlue)
        ) {
            Text("Save Expense")
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cancel")
        }
    }
}