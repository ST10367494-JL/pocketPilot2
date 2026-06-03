package com.pocketpilot.pocketpilot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.*
import com.pocketpilot.pocketpilot.ui.PocketViewModel
import com.pocketpilot.pocketpilot.ui.AuthViewModel
import com.pocketpilot.pocketpilot.ui.dashboard.DashboardScreen
import com.pocketpilot.pocketpilot.ui.expense.AddExpenseScreen
import com.pocketpilot.pocketpilot.ui.expense.ExpensesListScreen

import com.pocketpilot.pocketpilot.SpendingAnalyticsScreen


import com.pocketpilot.pocketpilot.ui.profile.ProfileScreen
import com.pocketpilot.pocketpilot.ui.auth.LoginScreen
import com.pocketpilot.pocketpilot.ui.auth.RegisterScreen
import com.pocketpilot.pocketpilot.ui.category.CategoryScreen
import com.pocketpilot.pocketpilot.ui.theme.PocketPilotTheme

/**
 * Root ComponentActivity driving the PocketPilot system shell.
 * Coordinates system windows and initializes the central Jetpack Navigation framework [Android Developers, 2024].
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PocketPilotTheme {
                // Initialize Firebase-connected ViewModels
                val pocketViewModel: PocketViewModel = viewModel()
                val authViewModel: AuthViewModel = viewModel()

                // Navigation Setup
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                // UI Performance Metrics pulled from state flows (Kotlin Flows, 2025)
                val expenseList by pocketViewModel.expenses.collectAsState(initial = emptyList())

                // ✅ FIXED REFERENCE MISMATCH: Changed it.amount to it.expense to align with database entities
                val totalSpent = expenseList.sumOf { it.expense.toDouble() }.toFloat()

                val monthlyBudget by pocketViewModel.monthlyBudget.collectAsState(initial = 0f)

                val progress = if (monthlyBudget > 0f) (totalSpent / monthlyBudget) else 0f
                val remaining = "R${(monthlyBudget - totalSpent).toInt()}"

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        // Hide BottomBar if currently on authentication pathways
                        if (currentDestination?.route !in listOf("login", "register")) {
                            NavigationBar {
                                NavigationBarItem(
                                    icon = { Icon(Icons.Default.Dashboard, "Dashboard") },
                                    label = { Text("Dashboard") },
                                    selected = currentDestination?.hierarchy?.any { it.route == "dashboard" } == true,
                                    onClick = {
                                        navController.navigate("dashboard") {
                                            popUpTo("dashboard") { saveState = true }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                                NavigationBarItem(
                                    icon = { Icon(Icons.Default.List, "Expenses") },
                                    label = { Text("Expenses") },
                                    selected = currentDestination?.hierarchy?.any { it.route == "expenses_list" } == true,
                                    onClick = {
                                        navController.navigate("expenses_list") {
                                            popUpTo("dashboard") { saveState = true }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                                NavigationBarItem(
                                    icon = { Icon(Icons.Default.BarChart, "Graphs") },
                                    label = { Text("Graphs") },
                                    selected = currentDestination?.hierarchy?.any { it.route == "analytics" } == true,
                                    onClick = {
                                        navController.navigate("analytics") {
                                            popUpTo("dashboard") { saveState = true }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                                NavigationBarItem(
                                    icon = { Icon(Icons.Default.Person, "Profile") },
                                    label = { Text("Profile") },
                                    selected = currentDestination?.hierarchy?.any { it.route == "profile" } == true,
                                    onClick = {
                                        navController.navigate("profile") {
                                            popUpTo("dashboard") { saveState = true }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "login",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        // --- AUTH ROUTES ---
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

                        // --- MAIN PORTAL SCREENS ---
                        composable("dashboard") {
                            DashboardScreen(
                                totalSpent = "R${totalSpent.toInt()}",
                                remaining = remaining,
                                progress = progress,
                                onNavigateToAddExpense = { navController.navigate("add_expense") },
                                onNavigateToCategories = { navController.navigate("categories") }
                            )
                        }
                        composable("expenses_list") {
                            ExpensesListScreen(viewModel = pocketViewModel)
                        }
                        composable("analytics") {
                            SpendingAnalyticsScreen()
                        }
                        composable("profile") {
                            ProfileScreen(
                                viewModel = pocketViewModel,
                                onLogout = {
                                    navController.navigate("login") { popUpTo(0) }
                                }
                            )
                        }

                        // --- SECONDARY UTILITY PATHS ---
                        // ✅ FIXED CUTOFF: Restored final navigation destinations cleanly
                        composable("add_expense") {
                            AddExpenseScreen(viewModel = pocketViewModel, onBack = { navController.popBackStack() })
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
