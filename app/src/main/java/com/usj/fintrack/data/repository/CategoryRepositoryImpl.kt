package com.usj.fintrack.data.repository

import com.usj.fintrack.data.dao.CategoryDao
import com.usj.fintrack.data.mapper.toDomain
import com.usj.fintrack.data.mapper.toEntity
import com.usj.fintrack.domain.model.Category
import com.usj.fintrack.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao
) : CategoryRepository {

    override fun getAllCategories(): Flow<List<Category>> =
        categoryDao.getAll().map { entities -> entities.map { it.toDomain() } }

    override suspend fun getCategoryById(id: Long): Category? =
        categoryDao.getById(id)?.toDomain()

    override suspend fun insertCategory(category: Category): Long =
        categoryDao.insert(category.toEntity())

    override suspend fun insertDefaultCategories(categories: List<Category>) =
        categoryDao.insertAll(categories.map { it.toEntity() })
}
