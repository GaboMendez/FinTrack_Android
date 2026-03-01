package com.usj.fintrack.domain.repository

import com.usj.fintrack.domain.model.Budget
import kotlinx.coroutines.flow.Flow

interface BudgetRepository {
    fun getAllBudgets(): Flow<List<Budget>>
    suspend fun getBudgetById(id: Long): Budget?
    suspend fun createBudget(budget: Budget): Long
    suspend fun updateBudget(budget: Budget)
    suspend fun deleteBudget(id: Long)
    fun getBudgetsByCategory(categoryId: Long): Flow<List<Budget>>
}
