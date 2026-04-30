package com.pocketpilot.pocketpilot.ui.expense

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.pocketpilot.pocketpilot.data.models.Expense
import com.pocketpilot.pocketpilot.utils.ExpenseCategories
import java.io.File
import java.io.IOException
import coil.compose.AsyncImage
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.ui.tooling.preview.Preview
import com.pocketpilot.pocketpilot.ui.theme.PocketPilotTheme

/**
 * Add Expense Screen for PocketPilot budget tracking app
 * References:
 * @see <a href="https://developer.android.com/jetpack/compose">Jetpack Compose Documentation</a>
 * @see <a href="https://developer.android.com/training/camera/photobasics">Camera Documentation</a>
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(
    onExpenseSaved: () -> Unit = {}
) {
    var amount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(ExpenseCategories.categories[0]) }
    var description by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(Date()) }
    var receiptPhotoUri by remember { mutableStateOf<Uri?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val locale = LocalConfiguration.current.locales[0]
    val dateFormat = remember(locale) { SimpleDateFormat("dd/MM/yyyy", locale) }

    var currentPhotoPath by remember { mutableStateOf<String?>(null) }

    // Camera launcher
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && currentPhotoPath != null) {
            val photoFile = File(currentPhotoPath!!)
            receiptPhotoUri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                photoFile
            )
        }
    }

    // Date picker
    if (showDatePicker) {
        DatePickerDialog(
            onDateSelected = { year, month, dayOfMonth ->
                val calendar = Calendar.getInstance()
                calendar.set(year, month, dayOfMonth)
                selectedDate = calendar.time
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Amount Input
        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Amount (R)") },
            placeholder = { Text("0.00") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            leadingIcon = { Text("R") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Category Dropdown
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = category,
                onValueChange = {},
                label = { Text("Category") },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                ExpenseCategories.categories.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(selectionOption) },
                        onClick = {
                            category = selectionOption
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Date Picker Button
        OutlinedButton(
            onClick = { showDatePicker = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Date: ${dateFormat.format(selectedDate)}")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Description
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description (Optional)") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2,
            maxLines = 4
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Camera Button
        Button(
            onClick = {
                try {
                    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
                    val imageFileName = "JPEG_${timeStamp}_"
                    val storageDir = context.getExternalFilesDir(null)
                    val photoFile = File.createTempFile(imageFileName, ".jpg", storageDir)
                    currentPhotoPath = photoFile.absolutePath

                    val photoURI = FileProvider.getUriForFile(
                        context,
                        "${context.packageName}.fileprovider",
                        photoFile
                    )
                    cameraLauncher.launch(photoURI)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Take Receipt Photo")
        }

        // Photo Preview
        if (receiptPhotoUri != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                AsyncImage(
                    model = receiptPhotoUri,
                    contentDescription = "Receipt Photo",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Save Button
        Button(
            onClick = {
                if (amount.toDoubleOrNull() != null && amount.toDouble() > 0) {
                    val expense = Expense(
                        amount = amount.toDouble(),
                        category = category,
                        description = description,
                        date = selectedDate,
                        receiptPath = currentPhotoPath
                    )
                    // TODO: Save to Room DB
                    // Using the variable to avoid unused variable warning
                    println("Saving expense: ${expense.amount}")
                    onExpenseSaved()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Expense")
        }
    }
}

@Composable
fun DatePickerDialog(
    onDateSelected: (year: Int, month: Int, dayOfMonth: Int) -> Unit,
    onDismiss: () -> Unit
) {
    val year = remember { Calendar.getInstance().get(Calendar.YEAR) }
    val month = remember { Calendar.getInstance().get(Calendar.MONTH) }
    val day = remember { Calendar.getInstance().get(Calendar.DAY_OF_MONTH) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = { onDateSelected(year, month, day) }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = { Text("Select Date") },
        text = { Text("Date picker would go here") }
    )
}

@Preview(showBackground = true)
@Composable
fun AddExpenseScreenPreview() {
    PocketPilotTheme {
        Surface {
            AddExpenseScreen()
        }
    }
}
