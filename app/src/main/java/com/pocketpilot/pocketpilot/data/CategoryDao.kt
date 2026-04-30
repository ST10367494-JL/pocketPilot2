package com.pocketpilot.pocketpilot.data

import androidx.room.*
import com.pocketpilot.pocketpilot.data.entities.Category
import kotlinx.coroutines.flow.Flow // Add this import

@Dao
interface CategoryDao {

    @Query("SELECT * FROM category")
    fun getAll(): Flow<List<Category>>

    @Query("SELECT * from category WHERE categoryId IN (:categoryIds)")
    fun findAllIds(categoryIds: IntArray): List<Category>

    @Query("SELECT * FROM category WHERE categoryName LIKE :categoryName LIMIT 1")
    fun findByName(categoryName: String): Category?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: Category)

    @Delete
    suspend fun delete(category: Category)
}