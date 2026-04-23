package com.pocketpilot.pocketpilot.data.entities

import android.accessibilityservice.GestureDescription
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.time.ZonedDateTime
import java.util.Date
import kotlin.time.Instant

@Entity
data class Expense(
    @PrimaryKey(autoGenerate = true) val expenseId: Long = 0,
    val description: String,
    val expense: Double,
    val receipt: String? = null,
    // This will be converted to a string by a TypeConverter see `PocketTypeConverters`
    val dateAdded: ZonedDateTime
)

@Entity(primaryKeys = ["expenseId", "categoryId"])
data class ExpenseWithCategoryCrossRef(
    val expenseId: Long,
    val categoryId: Long
)


data class ExpenseWithCategories(
    // Expense is an entity therefore it will be a reference
    // to the expense table and not include directly
    // in EntityWithCategory table
    @Embedded val expense: Expense,
    @Relation(
        parentColumn = "expenseId",
        entityColumn = "categoryId",
        entity = Category::class,
        associateBy = Junction(ExpenseWithCategoryCrossRef::class)
    ) val categories: List<Category>
)