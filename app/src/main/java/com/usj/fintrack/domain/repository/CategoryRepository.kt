package com.usj.fintrack.domain.repository

import com.usj.fintrack.domain.model.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getAllCategories(): Flow<List<Category>>
    suspend fun getCategoryById(id: Long): Category?
    suspend fun insertCategory(category: Category): Long
    suspend fun insertDefaultCategories(categories: List<Category>)
    suspend fun deleteCategory(id: Long)
    suspend fun updateCategory(category: Category)
}
