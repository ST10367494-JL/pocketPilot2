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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.*
import com.pocketpilot.pocketpilot.data.PocketDb
import com.pocketpilot.pocketpilot.ui.PocketViewModel
import com.pocketpilot.pocketpilot.ui.AuthViewModel
import com.pocketpilot.pocketpilot.ui.expense.AddExpenseScreen
import com.pocketpilot.pocketpilot.ui.expense.ExpensesListScreen
import com.pocketpilot.pocketpilot.ui.analytics.SpendingAnalyticsScreen
import com.pocketpilot.pocketpilot.ui.profile.ProfileScreen
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

                // Initialize ViewModels
                val pocketViewModel: PocketViewModel = viewModel(
                    factory = object : ViewModelProvider.Factory {
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return PocketViewModel(database.expenseDao()) as T
                        }
                    }
                )
                val authViewModel: AuthViewModel = viewModel()

                // Navigation Setup
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                // Logic for Dashboard data
                val expenseList by pocketViewModel.expenses.collectAsState()
                val totalSpent = expenseList.sumOf { it.expense }.toFloat()
                val progress = if (pocketViewModel.monthlyBudget > 0) (totalSpent / pocketViewModel.monthlyBudget).toFloat() else 0f
                val remaining = "R${(pocketViewModel.monthlyBudget - totalSpent).toInt()}"

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        // Only show BottomBar if not on Login/Register screens
                        if (currentDestination?.route !in listOf("login", "register")) {
                            NavigationBar {
                                NavigationBarItem(
                                    icon = { Icon(Icons.Default.Dashboard, "Dashboard") },
                                    label = { Text("Dashboard") },
                                    selected = currentDestination?.hierarchy?.any { it.route == "dashboard" } == true,
                                    onClick = { navController.navigate("dashboard") { popUpTo("dashboard") { saveState = true }; launchSingleTop = true; restoreState = true } }
                                )
                                NavigationBarItem(
                                    icon = { Icon(Icons.Default.List, "Expenses") },
                                    label = { Text("Expenses") },
                                    selected = currentDestination?.hierarchy?.any { it.route == "expenses_list" } == true,
                                    onClick = { navController.navigate("expenses_list") { popUpTo("dashboard") { saveState = true }; launchSingleTop = true; restoreState = true } }
                                )
                                NavigationBarItem(
                                    icon = { Icon(Icons.Default.BarChart, "Graphs") },
                                    label = { Text("Graphs") },
                                    selected = currentDestination?.hierarchy?.any { it.route == "analytics" } == true,
                                    onClick = { navController.navigate("analytics") { popUpTo("dashboard") { saveState = true }; launchSingleTop = true; restoreState = true } }
                                )
                                NavigationBarItem(
                                    icon = { Icon(Icons.Default.Person, "Profile") },
                                    label = { Text("Profile") },
                                    selected = currentDestination?.hierarchy?.any { it.route == "profile" } == true,
                                    onClick = { navController.navigate("profile") { popUpTo("dashboard") { saveState = true }; launchSingleTop = true; restoreState = true } }
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
                        // --- AUTH ---
                        composable("login") {
                            LoginScreen(
                                viewModel = authViewModel,
                                onNavigateToRegister = { navController.navigate("register") },
                                onLoginSuccess = { navController.navigate("dashboard") { popUpTo("login") { inclusive = true } } }
                            )
                        }
                        composable("register") {
                            RegisterScreen(
                                viewModel = authViewModel,
                                onNavigateToLogin = { navController.navigate("login") },
                                onRegisterSuccess = { navController.navigate("dashboard") { popUpTo("register") { inclusive = true } } }
                            )
                        }

                        // --- MAIN APP ---
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
                            ProfileScreen(onLogout = {
                                navController.navigate("login") { popUpTo(0) }
                            })
                        }

                        // --- SECONDARY SCREENS ---
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

@Composable
fun DashboardScreen(
    totalSpent: String,
    remaining: String,
    progress: Float,
    onNavigateToAddExpense: () -> Unit,
    onNavigateToCategories: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
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
            onClick = onNavigateToAddExpense,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = PocketBlue)
        ) {
            Text("+ Add Expense")
        }
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedButton(onClick = onNavigateToCategories, modifier = Modifier.fillMaxWidth()) {
            Text("Manage Categories")
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.androidx.room)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.pocketpilot.pocketpilot"

    compileSdkPreview = "Baklava"

    defaultConfig {
        applicationId = "com.pocketpilot.pocketpilot"
        minSdk = 25
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

@Composable
fun CategoryScreen(onBack: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(20.dp)) {
        Text("Manage Categories", style = MaterialTheme.typography.headlineMedium)
        Button(onClick = onBack, Modifier.padding(top = 20.dp)) {
            Text("Back to Dashboard")
        }
    }
dependencies {
    // Core Android & Compose
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.ui)
    implementation("androidx.navigation:navigation-compose:2.8.5")
    
    // UI Compatibility
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Room Database
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    // Image Loading
    implementation("io.coil-kt:coil-compose:2.7.0")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.0.0"))
    implementation("com.google.firebase:firebase-auth")

    // Testing
    testImplementation(libs.junit)
    testImplementation(libs.androidx.room.testing)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
