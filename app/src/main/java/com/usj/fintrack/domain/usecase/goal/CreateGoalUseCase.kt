package com.usj.fintrack.domain.usecase.goal

import com.usj.fintrack.domain.model.Goal
import com.usj.fintrack.domain.repository.GoalRepository
import javax.inject.Inject

class CreateGoalUseCase @Inject constructor(
    private val goalRepository: GoalRepository
) {
    suspend operator fun invoke(goal: Goal): Result<Long> {
        if (goal.targetAmount <= 0) {
            return Result.failure(IllegalArgumentException("Target amount must be positive"))
        }
        if (goal.deadline <= System.currentTimeMillis()) {
            return Result.failure(IllegalArgumentException("Deadline must be in the future"))
        }
        return runCatching { goalRepository.createGoal(goal) }
    }
}
