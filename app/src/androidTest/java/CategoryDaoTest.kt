package com.pocketpilot.pocketpilot

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.pocketpilot.pocketpilot.data.PocketDb
import com.pocketpilot.pocketpilot.data.CategoryDao
import com.pocketpilot.pocketpilot.data.entities.Category
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class CategoryDaoTest {

    private lateinit var db: PocketDb
    private lateinit var categoryDao: CategoryDao

    @Before
    fun createDb() {
        // Get the context of the app
        val context = ApplicationProvider.getApplicationContext<Context>()

        // Create an in-memory database (data is deleted when process ends)
        db = Room.inMemoryDatabaseBuilder(context, PocketDb::class.java)
            .allowMainThreadQueries()
            .build()

        categoryDao = db.categoryDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertAndReadCategory() = runTest {
        // 1. Create a Category object
        val category = Category(
            name = "Groceries",
            colour = "#FF0000"
        )

        // 2. Perform the Insert
        categoryDao.insert(category)

        // 3. Retrieve all categories
        val allCategories = categoryDao.getAll().first()

        // 4. Assertions (Verifying data is correct)
        assertEquals(1, allCategories.size)
        assertEquals("Groceries", allCategories[0].name)
        assertEquals("#FF0000", allCategories[0].colour)
    }

    @Test
    fun deleteCategory() = runTest {
        val category = Category(name = "Bills", colour = "#0000FF")
        categoryDao.insert(category)

        // Fetch it first to get the generated ID
        val savedCategory = categoryDao.getAll().first()[0]

        // Delete it
        categoryDao.delete(savedCategory)

        // Verify list is empty
        val allCategories = categoryDao.getAll().first()
        assertEquals(0, allCategories.size)
    }
}
