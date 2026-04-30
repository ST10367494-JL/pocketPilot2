package com.pocketpilot.pocketpilot.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.pocketpilot.pocketpilot.data.entities.Category

@Dao
interface CategoryDao {

    @Query("SELECT * FROM category")
    fun getAll(): List<Category>

    @Query("SELECT * from category WHERE categoryId IN (:categoryIds)")
    fun findAllIds(categoryIds: IntArray): List<Category>

    @Query("SELECT * FROM category WHERE name LIKE :categoryName LIMIT 1")
    fun findByName(categoryName: String): Category

    @Insert
    fun insertAll(vararg categories: Category)

    @Delete
    fun delete(category: Category)
}