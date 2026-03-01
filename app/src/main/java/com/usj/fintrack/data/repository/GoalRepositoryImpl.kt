package com.usj.fintrack.data.repository

import com.usj.fintrack.data.dao.GoalDao
import com.usj.fintrack.data.mapper.toDomain
import com.usj.fintrack.data.mapper.toEntity
import com.usj.fintrack.domain.model.Goal
import com.usj.fintrack.domain.repository.GoalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GoalRepositoryImpl @Inject constructor(
    private val goalDao: GoalDao
) : GoalRepository {

    override fun getAllGoals(): Flow<List<Goal>> =
        goalDao.getAll().map { entities -> entities.map { it.toDomain() } }

    override suspend fun getGoalById(id: Long): Goal? =
        goalDao.getById(id)?.toDomain()

    override suspend fun createGoal(goal: Goal): Long =
        goalDao.insert(goal.toEntity())

    override suspend fun updateGoal(goal: Goal) =
        goalDao.update(goal.toEntity())

    override suspend fun deleteGoal(id: Long) {
        val entity = goalDao.getById(id) ?: return
        goalDao.delete(entity)
    }
}
