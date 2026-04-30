package com.pocketpilot.pocketpilot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.pocketpilot.pocketpilot.data.PocketDb
import com.pocketpilot.pocketpilot.ui.PocketViewModel
import com.pocketpilot.pocketpilot.ui.AuthViewModel
import com.pocketpilot.pocketpilot.ui.expense.AddExpenseScreen
import com.pocketpilot.pocketpilot.ui.theme.PocketBlue
import com.pocketpilot.pocketpilot.ui.theme.PocketPilotTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PocketPilotTheme {
                val context = LocalContext.current
                val database = PocketDb.getDatabase(context)

                val pocketViewModel: PocketViewModel = viewModel(
                    factory = object : ViewModelProvider.Factory {
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            @Suppress("UNCHECKED_CAST")
                            return PocketViewModel(database.expenseDao()) as T
                        }
                    }
                )
                val authViewModel: AuthViewModel = viewModel()

                val expenseList by pocketViewModel.expenses.collectAsState()
                val totalSpent = expenseList.sumOf { it.expense }
                val progress = if (pocketViewModel.monthlyBudget > 0) (totalSpent / pocketViewModel.monthlyBudget).toFloat() else 0f
                val remaining = "R${(pocketViewModel.monthlyBudget - totalSpent).toInt()}"
                val navController = rememberNavController()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "login",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("login") {
                            LoginScreen(
                                viewModel = authViewModel,
                                onNavigateToRegister = { navController.navigate("register") },
                                onLoginSuccess = {
                                    navController.navigate("dashboard") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable("register") {
                            RegisterScreen(
                                viewModel = authViewModel,
                                onNavigateToLogin = { navController.navigate("login") },
                                onRegisterSuccess = {
                                    navController.navigate("dashboard") {
                                        popUpTo("register") { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable("dashboard") {
                            DashboardScreen(
                                totalSpent = "R${totalSpent.toInt()}",
                                remaining = remaining,
                                progress = progress,
                                onNavigateToExpenses = { navController.navigate("expenses") },
                                onNavigateToCategories = { navController.navigate("categories") }
                            )
                        }
                        composable("expenses") {
                            AddExpenseScreen(
                                onExpenseSaved = { navController.popBackStack() }
                            )
                        }
                        composable("categories") {
                            CategoryScreen(onBack = { navController.popBackStack() })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DashboardScreen(
    totalSpent: String,
    remaining: String,
    progress: Float,
    onNavigateToExpenses: () -> Unit,
    onNavigateToCategories: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Dashboard", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(40.dp))

        BudgetCircle(percentage = progress, remainingAmount = remaining)

        Spacer(modifier = Modifier.height(40.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Monthly Budget: R5000", style = MaterialTheme.typography.titleMedium)
                Text("Total Spent: $totalSpent", color = PocketBlue)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onNavigateToExpenses,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = PocketBlue)
        ) {
            Text("+ Add Expense")
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedButton(
            onClick = onNavigateToCategories,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Manage Categories")
        }
    }
}

@Composable
fun BudgetCircle(percentage: Float, remainingAmount: String) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(200.dp)) {
        CircularProgressIndicator(
            progress = { if (percentage > 1f) 1f else percentage },
            modifier = Modifier.fillMaxSize(),
            color = if (percentage > 0.9f) Color.Red else PocketBlue,
            strokeWidth = 12.dp
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "${(percentage * 100).toInt()}% Used", style = MaterialTheme.typography.headlineSmall)
            Text(text = "Remaining: $remainingAmount", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun CategoryScreen(onBack: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(20.dp)) {
        Text("Manage Categories", style = MaterialTheme.typography.headlineMedium)
        Button(onClick = onBack, Modifier.padding(top = 20.dp)) { Text("Back to Dashboard") }
    }
}
