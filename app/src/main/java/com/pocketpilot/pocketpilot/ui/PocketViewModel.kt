package com.pocketpilot.pocketpilot.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pocketpilot.pocketpilot.data.entities.Expense
import com.pocketpilot.pocketpilot.data.ExpenseDao
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PocketViewModel(private val expenseDao: ExpenseDao) : ViewModel() {

    // 1. Observe all expenses from DB as a StateFlow
// Change .getAllExpenses() to .getAll() to match your Dao
    val expenses: StateFlow<List<Expense>> = expenseDao.getAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val monthlyBudget = 5000.0

    // 2. Save a new expense to the DB
    fun addExpense(expense: Expense) {
        viewModelScope.launch {
            expenseDao.insert(expense)
        }
    }
}
