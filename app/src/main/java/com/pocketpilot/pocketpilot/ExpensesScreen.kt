package com.pocketpilot.pocketpilot.ui.expense

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pocketpilot.pocketpilot.data.entities.ExpenseWithCategories
import com.pocketpilot.pocketpilot.ui.theme.PocketBlue

/**
 * Interface view displaying live relational room database tracking records.
 * Integrates hardware camera capture result parameters for Custom Feature B [Android Developers, 2024].
 */
@Composable
fun ExpensesScreen(viewModel: ExpenseViewModel) {

    val expenses by viewModel.expensesList.collectAsState(initial = emptyList())

    // Custom Feature B: State hooks for managing hardware picture previews
    var receiptBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var operationFeedbackText by remember { mutableStateOf("No Receipt Image Attached") }

    // Creates the camera launcher intent pipeline to bypass manually tracking system lifecycle channels (Android Developers, 2024)
    val nativeCameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        if (bitmap != null) {
            receiptBitmap = bitmap
            operationFeedbackText = "✅ Receipt Verification Attached!"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Expenses Matrix",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E3A8A)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = { },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Filter by Date: Select Month 🔽", color = Color(0xFF1E3A8A))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Infinite lazy data stream populating relational profile instances
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(expenses) { item: ExpenseWithCategories ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F4F6)),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {

                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val categoryText = item.categories.joinToString { it.categoryName }

                        Column {
                            Text(text = categoryText, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)

                            // CHANGED LINE: Removed the broken '.id' reference to clear the crash safely
                            Text(text = "Logged Transaction", fontSize = 11.sp, color = Color.Gray)
                        }

                        Text(
                            text = "R${item.expense.expense.toInt()}",
                            fontWeight = FontWeight.Bold,
                            color = PocketBlue,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }

        // Camera upload item slot container block
        receiptBitmap?.let { bitmap ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .padding(top = 8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(8.dp).fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Receipt Image Capture View",
                        modifier = Modifier.size(70.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("Live Receipt Preview", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        Text(
            text = operationFeedbackText,
            fontSize = 12.sp,
            color = if (receiptBitmap != null) Color(0xFF16A34A) else Color.DarkGray,
            modifier = Modifier.padding(top = 8.dp).align(Alignment.CenterHorizontally)
        )

        // Custom Feature B Execution Vector: Triggers your phone's physical hardware camera
        Button(
            onClick = { nativeCameraLauncher.launch(null) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E3A8A))
        ) {
            Icon(Icons.Default.AddAPhoto, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Upload Image", fontWeight = FontWeight.Bold)
        }
    }
}
