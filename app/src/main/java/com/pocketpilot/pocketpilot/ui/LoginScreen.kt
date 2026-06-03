package com.pocketpilot.pocketpilot.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pocketpilot.pocketpilot.ui.AuthViewModel

/**
 * Authentication login user entry screen.
 * UI elements and alignments mapped directly from your design document wireframes [Android Developers, 2024].
 */
@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // App Identity Header as per blueprint logo text block
        Text(
            text = "PocketPilot",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E3A8A) // PocketPilot Corporate Identity Blue
        )

        Spacer(Modifier.height(32.dp))

        // Wireframe Element: Username Field Component Box
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        )

        Spacer(Modifier.height(12.dp))

        // Wireframe Element: Password Field Component Box
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        )

        errorMessage?.let {
            Spacer(Modifier.height(8.dp))
            Text(it, color = MaterialTheme.colorScheme.error, fontSize = 14.sp)
        }

        Spacer(Modifier.height(24.dp))

        // Primary Action Button Execution Vector
        Button(
            onClick = {
                // Mapping username proxy parameter as email stream variable for Firebase rules (Firebase, 2026)
                val emailPayload = if (username.contains("@")) username else "$username@pocketpilot.com"
                viewModel.loginUser(emailPayload, password, onLoginSuccess, { errorMessage = it })
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E3A8A)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Log In", fontSize = 16.sp, color = Color.White)
        }

        Spacer(Modifier.height(16.dp))

        Text(text = "Don't have an account?", color = Color.Gray, fontSize = 14.sp)
        TextButton(onClick = onNavigateToRegister) {
            Text("Register", color = Color(0xFF1E3A8A), fontWeight = FontWeight.Bold)
        }
    }
}
