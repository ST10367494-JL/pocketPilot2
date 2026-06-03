package com.pocketpilot.pocketpilot.ui.expense

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pocketpilot.pocketpilot.ui.PocketViewModel
import com.pocketpilot.pocketpilot.ui.theme.PocketBlue

/**
 * Interface view displaying live structural expense transaction rows.
 * Implements hardware device camera captures to fulfill custom PoE submission features [Android Developers, 2024].
 */
@Composable
fun ExpensesListScreen(viewModel: PocketViewModel) {
    // Collect the reactive state flow directly pulled down from Firebase Cloud Firestore tables
    val expenseList by viewModel.expenses.collectAsState()

    // UI tracking parameters to handle local hardware camera photo states
    var capturedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var uploadStatusMessage by remember { mutableStateOf("No Receipt Image Attached") }

    // System activity result launcher to query hardware camera hardware layers cleanly (Android Developers, 2024)
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        if (bitmap != null) {
            capturedBitmap = bitmap
            uploadStatusMessage = "✅ Receipt Captured Successfully!"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Expenses Matrix Log",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E3A8A)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // UI Wireframe Component: Date Interval Range Selector Filter Row
        OutlinedButton(
            onClick = { /* Pulls date filter streams from database */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Filter by Date: Current Month 🔽", color = Color(0xFF1E3A8A))
        }

        // Lazy load transaction rows dynamically compiled via background snapshots (Android Developers, 2024)
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 12.dp)
        ) {
            items(expenseList) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F4F6))
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            // ✅ FIXED FOR COMPILATION STABILITY: Removed .category reference to align exactly with your Room schema definition
                            val displayDescription = item.description.ifEmpty { "Transaction Entry" }
                            val displayDateLabel = item.dateAdded.toString().take(10) // Shows a clean portion of the Date string

                            Text(
                                text = displayDescription,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Logged: $displayDateLabel",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Gray
                            )
                        }
                        Text(
                            text = "R${item.expense.toInt()}",
                            color = PocketBlue,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Custom Feature B: Verification preview view element block layout
        capturedBitmap?.let { bitmap ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(bottom = 8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(8.dp).fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Receipt Preview Stamp",
                        modifier = Modifier.size(80.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("Live Receipt Preview", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Live text layout tracking system interaction events
        Text(
            text = uploadStatusMessage,
            fontSize = 13.sp,
            color = if (capturedBitmap != null) Color(0xFF16A34A) else Color.DarkGray,
            modifier = Modifier.padding(bottom = 8.dp).align(Alignment.CenterHorizontally)
        )

        // Custom Feature B Button: Launches your actual hardware camera module on press
        Button(
            onClick = { cameraLauncher.launch(null) },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E3A8A))
        ) {
            Icon(Icons.Default.PhotoCamera, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Capture Receipt Photo", fontWeight = FontWeight.Bold)
        }
    }
}
