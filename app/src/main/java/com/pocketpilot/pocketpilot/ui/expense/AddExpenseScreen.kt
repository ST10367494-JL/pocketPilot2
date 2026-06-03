package com.pocketpilot.pocketpilot.ui.expense

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.KeyboardOptions
import com.pocketpilot.pocketpilot.ui.PocketViewModel

/**
 * Data input form screen allowing users to track and record transactions.
 * Saves values directly onto Cloud Firestore database instances [Firebase, 2026].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(viewModel: PocketViewModel, onBack: () -> Unit) {
    // Input component field tracker state variables [Android Developers, 2024]
    var amountText by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Groceries") }
    var noteText by remember { mutableStateOf("") }

    var dropdownExpanded by remember { mutableStateOf(false) }
    val categoriesList = listOf("Groceries", "Transport", "Food", "Entertainment", "Other")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Expense", fontWeight = FontWeight.Bold, color = Color(0xFF1E3A8A)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Go Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Wireframe Form Component 1: Monetary Transaction Amount Entry Field Box
            OutlinedTextField(
                value = amountText,
                onValueChange = { amountText = it },
                label = { Text("Amount (ZAR)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Wireframe Form Component 2: Structural Category Dropdown Selector Component Box
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = selectedCategory,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Category") },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Expand Menu Options",
                            modifier = Modifier.clickable { dropdownExpanded = true }
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )

                DropdownMenu(
                    expanded = dropdownExpanded,
                    onDismissRequest = { dropdownExpanded = false },
                    modifier = Modifier.fillMaxWidth(0.85f)
                ) {
                    categoriesList.forEach { categoryItemName ->
                        DropdownMenuItem(
                            text = { Text(categoryItemName) },
                            onClick = {
                                selectedCategory = categoryItemName
                                dropdownExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Wireframe Form Component 3: Brief Text Note Description Memo Box
            OutlinedTextField(
                value = noteText,
                onValueChange = { noteText = it },
                label = { Text("Reference / Note Description") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Verification execution button triggering state collection mapping tasks
            Button(
                onClick = {
                    val parsedAmountValue = amountText.toFloatOrNull() ?: 0f
                    if (parsedAmountValue > 0f) {
                        // Push standard structured parameters up onto the cloud storage platform
                        viewModel.addExpenseToCloud(
                            amount = parsedAmountValue,
                            category = selectedCategory,
                            note = noteText
                        )
                        onBack() // Step backward along navigation backstack history lanes
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E3A8A)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Save Expense Record", fontSize = 16.sp, color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}
