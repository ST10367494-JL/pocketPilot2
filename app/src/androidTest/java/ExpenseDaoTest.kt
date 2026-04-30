package com.pocketpilot.pocketpilot

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.pocketpilot.pocketpilot.data.PocketDb
import com.pocketpilot.pocketpilot.data.ExpenseDao
import com.pocketpilot.pocketpilot.data.entities.Expense
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
class ExpenseDaoTest {
    private lateinit var db: PocketDb
    private lateinit var expenseDao: ExpenseDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        // Create a temporary in-memory database
        db = Room.inMemoryDatabaseBuilder(context, PocketDb::class.java).build()
        expenseDao = db.expenseDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertAndReadExpense() = runTest {
        val expense = Expense(
            description = "Lunch",
            expense = 150.0,
            dateAdded = Date()
        )
        // Test the insert
        expenseDao.insert(expense)

        // Test the read (getAll returns a Flow, so we take the first emission)
        val allExpenses = expenseDao.getAll().first()
        assertEquals(allExpenses[0].description, "Lunch")
        assertEquals(allExpenses[0].expense, 150.0, 0.0)
    }
}
