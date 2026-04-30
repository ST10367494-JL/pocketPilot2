package com.pocketpilot.pocketpilot.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.pocketpilot.pocketpilot.data.entities.Category
import com.pocketpilot.pocketpilot.data.entities.Expense
import com.pocketpilot.pocketpilot.data.entities.ExpenseWithCategories
import com.pocketpilot.pocketpilot.data.entities.ExpenseWithCategoryCrossRef
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    // ✅ Get all expenses
    @Query("SELECT * FROM expense")
    fun getAll(): Flow<List<Expense>>

    // ✅ Get expenses with categories (many-to-many)
    @Transaction
    @Query("SELECT * FROM expense")
    fun getAllExpensesWithCategories(): Flow<List<ExpenseWithCategories>>

    // ✅ Get single expense
    @Query("SELECT * FROM expense WHERE expenseId = :expenseId")
    suspend fun getExpenseById(expenseId: Long): Expense

    // ✅ Get single expense WITH categories
    @Transaction
    @Query("SELECT * FROM expense WHERE expenseId = :expenseId")
    fun getExpenseByIdWithCategories(expenseId: Long): Flow<ExpenseWithCategories>

    // ✅ Insert expense
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(expense: Expense): Long

    // ✅ Insert cross refs (link expense ↔ categories)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpenseCategoryCrossRef(vararg crossref: ExpenseWithCategoryCrossRef)

    // ✅ Helper to link expense to categories
    @Transaction
    suspend fun addExpenseToCategories(expense: Expense, vararg categories: Category) {
        val expenseId = insert(expense) // ensure ID exists

        val crossrefs = categories.map { category ->
            ExpenseWithCategoryCrossRef(expenseId, category.categoryId)
        }

        insertExpenseCategoryCrossRef(*crossrefs.toTypedArray())
    }

    // ✅ FIXED: Update must have annotation
    @Update
    suspend fun updateExpense(updatedExpense: Expense)
}