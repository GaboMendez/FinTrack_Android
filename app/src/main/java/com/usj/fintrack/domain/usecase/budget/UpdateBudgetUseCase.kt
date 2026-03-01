package com.usj.fintrack.domain.usecase.budget

import com.usj.fintrack.domain.model.Budget
import com.usj.fintrack.domain.repository.BudgetRepository
import javax.inject.Inject

class UpdateBudgetUseCase @Inject constructor(
    private val budgetRepository: BudgetRepository
) {
    suspend operator fun invoke(budget: Budget): Result<Unit> {
        if (budget.limitAmount <= 0) {
            return Result.failure(IllegalArgumentException("Limit amount must be positive"))
        }
        if (budget.periodEndDate <= budget.periodStartDate) {
            return Result.failure(IllegalArgumentException("End date must be after start date"))
        }
        return runCatching { budgetRepository.updateBudget(budget) }
    }
}
