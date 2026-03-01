package com.usj.fintrack.domain.usecase.goal

import com.usj.fintrack.domain.model.Goal
import com.usj.fintrack.domain.repository.GoalRepository
import javax.inject.Inject

class AddGoalContributionUseCase @Inject constructor(
    private val goalRepository: GoalRepository
) {
    suspend operator fun invoke(goalId: Long, amount: Double): Result<Goal> {
        if (amount <= 0) {
            return Result.failure(IllegalArgumentException("Contribution amount must be positive"))
        }

        val goal = goalRepository.getGoalById(goalId)
            ?: return Result.failure(IllegalArgumentException("Goal not found"))

        if (goal.isCompleted) {
            return Result.failure(IllegalStateException("Goal is already completed"))
        }

        val newCurrentAmount = goal.currentAmount + amount
        val isCompleted = newCurrentAmount >= goal.targetAmount
        val updatedGoal = goal.copy(
            currentAmount = newCurrentAmount,
            isCompleted = isCompleted
        )

        return runCatching {
            goalRepository.updateGoal(updatedGoal)
            updatedGoal
        }
    }
}
