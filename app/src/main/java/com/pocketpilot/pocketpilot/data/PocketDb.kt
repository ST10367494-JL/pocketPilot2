package com.pocketpilot.pocketpilot.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.pocketpilot.pocketpilot.data.entities.Category
import com.pocketpilot.pocketpilot.data.entities.Expense
import com.pocketpilot.pocketpilot.data.entities.ExpenseWithCategoryCrossRef

import com.pocketpilot.pocketpilot.data.CategoryDao

import com.pocketpilot.pocketpilot.data.ExpenseDao


@Database(
    entities = [
        Category::class,
        Expense::class,
        ExpenseWithCategoryCrossRef::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(PocketTypeConverters::class)


abstract class PocketDb : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun expenseDao(): ExpenseDao

    companion object {
        @Volatile
        private var INSTANCE: PocketDb? = null

        fun getDatabase(context: Context): PocketDb {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PocketDb::class.java,
                    "pocket_database"
                )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
