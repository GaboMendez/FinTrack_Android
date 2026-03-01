package com.usj.fintrack.domain.usecase.goal

import com.usj.fintrack.domain.model.Goal
import com.usj.fintrack.domain.repository.GoalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGoalsUseCase @Inject constructor(
    private val goalRepository: GoalRepository
) {
    operator fun invoke(): Flow<List<Goal>> = goalRepository.getAllGoals()
}
