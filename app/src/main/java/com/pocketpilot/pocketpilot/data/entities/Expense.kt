package com.pocketpilot.pocketpilot.data.entities

import androidx.room.*
import java.util.Date

@Entity
data class Expense(
    @PrimaryKey(autoGenerate = true) val expenseId: Long = 0,
    val description: String,
    val expense: Double,
    val receipt: String? = null,
    val dateAdded: Date
)

// 1. This is the "Junction" table that connects Expenses to Categories
@Entity(
    primaryKeys = ["expenseId", "categoryId"],
    indices = [Index(value = ["categoryId"])]
)
data class ExpenseWithCategoryCrossRef(
    val expenseId: Long,
    val categoryId: Long
)

// 2. This is a helper class to SHOW an expense along with its categories
data class ExpenseWithCategories(
    @Embedded val expense: Expense,
    @Relation(
        parentColumn = "expenseId",
        entityColumn = "categoryId",
        associateBy = Junction(ExpenseWithCategoryCrossRef::class)
    )
    val categories: List<Category>
)
