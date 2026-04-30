package com.pocketpilot.pocketpilot.ui.expense

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pocketpilot.pocketpilot.data.ExpenseDao
import com.pocketpilot.pocketpilot.data.entities.ExpenseWithCategories
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ExpenseViewModel(private val dao: ExpenseDao) : ViewModel() {

    // ✅ Convert Flow → StateFlow (better for Compose)
    val expensesList: StateFlow<List<ExpenseWithCategories>> =
        dao.getAllExpensesWithCategories()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    // ✅ Update receipt
    fun updateReceipt(expenseWithCategories: ExpenseWithCategories, uri: String) {
        viewModelScope.launch {
            val updatedExpense = expenseWithCategories.expense.copy(
                receipt = uri
            )
            dao.updateExpense(updatedExpense)
        }
    }
}