package com.usj.fintrack.domain.usecase.goal

import com.usj.fintrack.domain.model.Goal
import com.usj.fintrack.domain.repository.GoalRepository
import javax.inject.Inject

class GetGoalByIdUseCase @Inject constructor(
    private val goalRepository: GoalRepository
) {
    suspend operator fun invoke(id: Long): Goal? = goalRepository.getGoalById(id)
}
