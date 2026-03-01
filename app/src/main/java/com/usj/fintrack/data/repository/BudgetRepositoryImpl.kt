package com.usj.fintrack.data.repository

import com.usj.fintrack.data.dao.BudgetDao
import com.usj.fintrack.data.mapper.toDomain
import com.usj.fintrack.data.mapper.toEntity
import com.usj.fintrack.domain.model.Budget
import com.usj.fintrack.domain.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BudgetRepositoryImpl @Inject constructor(
    private val budgetDao: BudgetDao
) : BudgetRepository {

    override fun getAllBudgets(): Flow<List<Budget>> =
        budgetDao.getAll().map { entities -> entities.map { it.toDomain() } }

    override suspend fun getBudgetById(id: Long): Budget? =
        budgetDao.getById(id)?.toDomain()

    override suspend fun createBudget(budget: Budget): Long =
        budgetDao.insert(budget.toEntity())

    override suspend fun updateBudget(budget: Budget) =
        budgetDao.update(budget.toEntity())

    override suspend fun deleteBudget(id: Long) {
        val entity = budgetDao.getById(id) ?: return
        budgetDao.delete(entity)
    }

    override fun getBudgetsByCategory(categoryId: Long): Flow<List<Budget>> =
        budgetDao.getByCategory(categoryId).map { entities -> entities.map { it.toDomain() } }
}
