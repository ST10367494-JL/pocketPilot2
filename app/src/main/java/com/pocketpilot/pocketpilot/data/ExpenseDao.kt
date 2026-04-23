package com.pocketpilot.pocketpilot.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.pocketpilot.pocketpilot.data.entities.Category
import com.pocketpilot.pocketpilot.data.entities.Expense
import com.pocketpilot.pocketpilot.data.entities.ExpenseWithCategories
import com.pocketpilot.pocketpilot.data.entities.ExpenseWithCategoryCrossRef

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expense")
    fun getAll(): List<Expense>


    // the return type ExpenseWithCategories will automatically resolve the all categories
    // even though it is not directly queried.
    @Transaction // Use transaction as RoomDB will run two queries
    @Query("SELECT * FROM expense")
    fun getAllExpensesWithCategories(): List<ExpenseWithCategories>

    @Query("SELECT * FROM expense WHERE expenseId = :expenseId")
    fun getExpenseById(expenseId: Long): Expense

    // FIXME(hadley): This seems to be broken as it is not resolving the relation correct but
    //  the data in the junction table is being created.
    @Transaction
    @Query("SELECT * FROM expense WHERE expenseId = :expenseId")
    fun getExpenseByIdWithCategories(expenseId: Long): ExpenseWithCategories

    @Insert
    fun insert(expense: Expense): Long

    /**
     * Do use outside of Dao
     *
     * this is a helper function
     * @see getAllExpensesWithCategories
     */
    @Insert
    fun _insertExpenseCategoryCrossRef(vararg crossref: ExpenseWithCategoryCrossRef)

    /**
     * @param expense expense to add categories to
     * @param categories 1 or more Categories
     *
     * Usage:
     *
     * - `addExpenseToCategories(expense1, category1)`
     * - `addExpenseToCategories(expense1, category1, category2)`
     */
    @Transaction
    fun addExpenseToCategories(expense: Expense, vararg categories: Category) {
        // Create an empty list
        var crossrefs = mutableListOf<ExpenseWithCategoryCrossRef>();
        for (category in categories) {
            crossrefs.add(ExpenseWithCategoryCrossRef(expense.expenseId, category.categoryId))
        }

        // the '*' operator expands an 'Array' not a list into a vararg
        _insertExpenseCategoryCrossRef(*(crossrefs.toTypedArray()))

    }
}