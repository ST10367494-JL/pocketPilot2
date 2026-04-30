package com.pocketpilot.pocketpilot

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.pocketpilot.pocketpilot.data.PocketDb
import com.pocketpilot.pocketpilot.data.ExpenseDao
import com.pocketpilot.pocketpilot.data.CategoryDao
import com.pocketpilot.pocketpilot.data.entities.Expense
import com.pocketpilot.pocketpilot.data.entities.Category
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.Date

@RunWith(AndroidJUnit4::class)
class DatabaseTests {

    private lateinit var db: PocketDb
    private lateinit var expenseDao: ExpenseDao
    private lateinit var categoryDao: CategoryDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, PocketDb::class.java)
            .allowMainThreadQueries()
            .build()
        expenseDao = db.expenseDao()
        categoryDao = db.categoryDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertAndGetExpense() = runTest {
        val expense = Expense(
            description = "Lunch",
            expense = 150.0,
            dateAdded = Date() // Use Date() instead of ZonedDateTime
        )
        val id = expenseDao.insert(expense)
        val result = expenseDao.getExpenseById(id)
        assertEquals("Lunch", result.description)
    }

    @Test
    fun testExpenseWithCategoryRelationship() = runTest {
        // Use individual inserts since your DAO names them 'insert'
        val category = Category(name = "Food", colour = "#FF0000")
        categoryDao.insert(category)

        val expense = Expense(
            expenseId = 1,
            description = "Pizza",
            expense = 200.0,
            dateAdded = Date()
        )
        expenseDao.insert(expense)

        // Link them
        expenseDao.addExpenseToCategories(expense, category)

        // Verify relationship
        val results = expenseDao.getAllExpensesWithCategories().first()
        assertEquals(1, results[0].categories.size)
        assertEquals("Food", results[0].categories[0].name)
    }
}
