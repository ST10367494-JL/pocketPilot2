package com.pocketpilot.pocketpilot.data.models

import java.util.Date

/**
 * Expense data model for PocketPilot budget tracking app
 *
 * Member 2 - Data Manager: Expense Management & Logic
 * Reference: Android Room documentation
 * @see <a href="https://developer.android.com/training/data-storage/room">Room Documentation</a>
 */
data class Expense(
    val id: Long = 0,
    val amount: Double,
    val category: String,
    val description: String = "",
    val date: Date,
    val receiptPath: String? = null,
    val createdAt: Date = Date()
)