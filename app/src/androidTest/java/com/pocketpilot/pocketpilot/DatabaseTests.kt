package com.pocketpilot.pocketpilot

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.services.storage.internal.TestStorageUtil
import com.pocketpilot.pocketpilot.data.CategoryDao
import com.pocketpilot.pocketpilot.data.ExpenseDao
import com.pocketpilot.pocketpilot.data.PocketDb
import com.pocketpilot.pocketpilot.data.entities.Category
import com.pocketpilot.pocketpilot.data.entities.Expense
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.io.IOException
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.jvm.Throws

@RunWith(AndroidJUnit4::class)
class DatabaseTests {
    private lateinit var categoryDao: CategoryDao
    private lateinit var expenseDao: ExpenseDao
    private lateinit var db: PocketDb

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.databaseBuilder<PocketDb>(context, "testdb").build()
        categoryDao = db.categoryDao()
        expenseDao = db.expenseDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun createExpenseAndReadBack() {
        val zone: ZoneId = ZoneId.systemDefault()
        val expense: Expense = Expense(description ="Bought apples", expense = 20.5, dateAdded = ZonedDateTime.now(zone))

        val id1 = expenseDao.insert(expense)
        val expenseResult = expenseDao.getExpenseById(id1)

        assertThat(expense.description, equalTo(expenseResult.description))
        assertThat(expense.expense, equalTo(expenseResult.expense))
        assertThat(expense.dateAdded, equalTo(expenseResult.dateAdded))
    }

    @Test
    @Throws(Exception::class)
    fun createCategories() {
        val category = Category(name="Food", colour="red")
        categoryDao.insertAll(category)
        val categoryResult = categoryDao.findByName("Food")
        assertThat(category.name, equalTo(categoryResult.name))
        assertThat(category.colour, equalTo(categoryResult.colour))
    }

    @Test
    @Throws(Exception::class)
    fun addExpenseToCategory() {
        val category = Category(name="Food", colour="red")
        categoryDao.insertAll(category)
        val categoryResult = categoryDao.findByName("Food")

        val zone: ZoneId = ZoneId.systemDefault()
        var expense: Expense = Expense(description ="Bought apples", expense = 20.5, dateAdded = ZonedDateTime.now(zone))
        expense = expenseDao.getExpenseById(expenseDao.insert(expense))
        expenseDao.addExpenseToCategories(expense, category)
        val expenseWithCategory = expenseDao.getExpenseByIdWithCategories(expense.expenseId);
        println(expenseWithCategory)
        assert(expenseWithCategory.categories.contains(categoryResult))

    }
}