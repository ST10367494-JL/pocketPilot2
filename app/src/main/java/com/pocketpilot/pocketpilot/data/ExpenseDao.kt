package com.pocketpilot.pocketpilot.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.OnConflictStrategy
import com.pocketpilot.pocketpilot.data.entities.Category
import com.pocketpilot.pocketpilot.data.entities.Expense
import com.pocketpilot.pocketpilot.data.entities.ExpenseWithCategories
import com.pocketpilot.pocketpilot.data.entities.ExpenseWithCategoryCrossRef
import kotlinx.coroutines.flow.Flow // CRITICAL for real-time updates

@Dao
interface ExpenseDao {

    // Changed to Flow so your Dashboard updates automatically!
    @Query("SELECT * FROM expense")
    fun getAll(): Flow<List<Expense>>

    @Transaction
    @Query("SELECT * FROM expense")
    fun getAllExpensesWithCategories(): Flow<List<ExpenseWithCategories>>

    @Query("SELECT * FROM expense WHERE expenseId = :expenseId")
    suspend fun getExpenseById(expenseId: Long): Expense

    @Transaction
    @Query("SELECT * FROM expense WHERE expenseId = :expenseId")
    fun getExpenseByIdWithCategories(expenseId: Long): Flow<ExpenseWithCategories>

    // Use OnConflictStrategy to avoid crashes on duplicate IDs
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(expense: Expense): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun _insertExpenseCategoryCrossRef(vararg crossref: ExpenseWithCategoryCrossRef)

    @Transaction
    suspend fun addExpenseToCategories(expense: Expense, vararg categories: Category) {
        val crossrefs = categories.map { category ->
            ExpenseWithCategoryCrossRef(expense.expenseId, category.categoryId)
        }
        _insertExpenseCategoryCrossRef(*(crossrefs.toTypedArray()))
    }
}
