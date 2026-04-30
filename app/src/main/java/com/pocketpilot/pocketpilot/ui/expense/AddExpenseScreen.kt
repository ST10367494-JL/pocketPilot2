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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.pocketpilot.pocketpilot.data.entities.Expense // Corrected import
import com.pocketpilot.pocketpilot.utils.ExpenseCategories
import java.io.File
import java.io.IOException
import coil.compose.AsyncImage
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.tooling.preview.Preview
import com.pocketpilot.pocketpilot.ui.theme.PocketPilotTheme
import com.pocketpilot.pocketpilot.ui.PocketViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(
    viewModel: PocketViewModel,
    onBack: () -> Unit = {}
) {
    var amount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(ExpenseCategories.categories[0]) }
    var description by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(Date()) }
    var receiptPhotoUri by remember { mutableStateOf<Uri?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", LocalLocale.current.platformLocale)
    var currentPhotoPath by remember { mutableStateOf<String?>(null) }

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

        OutlinedTextField(
            value = category,
            onValueChange = {},
            label = { Text("Category") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = { showDatePicker = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Date: ${dateFormat.format(selectedDate)}")
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description (Optional)") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2,
            maxLines = 4
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                try {
                    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
                    val storageDir = context.getExternalFilesDir(null)
                    val photoFile = File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
                    currentPhotoPath = photoFile.absolutePath
                    val photoURI = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", photoFile)
                    cameraLauncher.launch(photoURI)
                } catch (e: IOException) { e.printStackTrace() }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Take Receipt Photo")
        }

        if (receiptPhotoUri != null) {
            Card(modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
                AsyncImage(
                    model = receiptPhotoUri,
                    contentDescription = "Receipt",
                    modifier = Modifier.fillMaxWidth().height(200.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Fixed Save Button Logic
        Button(
            onClick = {
                val amtValue = amount.toDoubleOrNull()
                if (amtValue != null && amtValue > 0) {
                    val newExpense = Expense(
                        description = description,
                        expense = amtValue, // Matches your entity field
                        receipt = currentPhotoPath,
                        dateAdded = selectedDate
                    )
                    // Save to DB before navigating back
                    viewModel.addExpense(newExpense)
                    onBack()
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
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                val cal = Calendar.getInstance()
                onDateSelected(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
            }) { Text("OK") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
        title = { Text("Select Date") },
        text = { Text("Click OK to select today's date.") }
    )
}

@Preview(showBackground = true)
@Composable
fun AddExpensePreview() {
    PocketPilotTheme {
        Surface {
            // This is just for previewing, it won't actually save
            Text("Preview Mode")
        }
    }
}
