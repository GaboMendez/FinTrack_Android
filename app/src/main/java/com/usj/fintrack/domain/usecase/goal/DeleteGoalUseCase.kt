package com.usj.fintrack.domain.usecase.goal

import com.usj.fintrack.domain.repository.GoalRepository
import javax.inject.Inject

class DeleteGoalUseCase @Inject constructor(
    private val goalRepository: GoalRepository
) {
    suspend operator fun invoke(id: Long): Result<Unit> =
        runCatching { goalRepository.deleteGoal(id) }
}
