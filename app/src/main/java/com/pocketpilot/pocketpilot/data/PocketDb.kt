package com.pocketpilot.pocketpilot.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.pocketpilot.pocketpilot.data.entities.Category
import com.pocketpilot.pocketpilot.data.entities.Expense
import com.pocketpilot.pocketpilot.data.entities.ExpenseWithCategoryCrossRef

@Database(
    entities = [
        Category::class,
        Expense::class,
        ExpenseWithCategoryCrossRef::class
    ],
    version = 1
)
@TypeConverters(PocketTypeConverters::class)
abstract class PocketDb : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun expenseDao(): ExpenseDao
}

/**
 * Helper function to create a new database called pocketdb
 * @param context the Android context (use Activity.applicationContext)
 * @see android.content.ContextWrapper.getApplicationContext
 */
fun createDatabase(context: Context): PocketDb {
    return Room
        .databaseBuilder<PocketDb>(context, "pocketdb")
        .build()
}