package com.usj.fintrack.domain.usecase.goal

import com.usj.fintrack.domain.model.Goal
import com.usj.fintrack.domain.repository.GoalRepository
import javax.inject.Inject

class UpdateGoalUseCase @Inject constructor(
    private val goalRepository: GoalRepository
) {
    suspend operator fun invoke(goal: Goal): Result<Unit> {
        if (goal.targetAmount <= 0) {
            return Result.failure(IllegalArgumentException("Target amount must be positive"))
        }
        return runCatching { goalRepository.updateGoal(goal) }
    }
}
