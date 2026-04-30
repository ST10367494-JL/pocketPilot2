package com.pocketpilot.pocketpilot.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Category(
    @PrimaryKey(autoGenerate = true) val categoryId: Long = 0,
    val categoryName: String,
    val colour: String
)