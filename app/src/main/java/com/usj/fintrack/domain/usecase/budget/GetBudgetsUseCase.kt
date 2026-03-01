package com.usj.fintrack.domain.usecase.budget

import com.usj.fintrack.domain.model.Budget
import com.usj.fintrack.domain.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBudgetsUseCase @Inject constructor(
    private val budgetRepository: BudgetRepository
) {
    operator fun invoke(): Flow<List<Budget>> = budgetRepository.getAllBudgets()
}
