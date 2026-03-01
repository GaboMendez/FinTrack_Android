package com.usj.fintrack.domain.usecase.budget

import com.usj.fintrack.domain.model.Budget
import com.usj.fintrack.domain.model.BudgetStatus
import javax.inject.Inject

class CheckBudgetStatusUseCase @Inject constructor() {
    operator fun invoke(budget: Budget): BudgetStatus {
        val percentageUsed = if (budget.limitAmount > 0) {
            budget.spentAmount / budget.limitAmount
        } else {
            0.0
        }
        val remainingAmount = budget.limitAmount - budget.spentAmount
        return BudgetStatus(
            budget = budget,
            percentageUsed = percentageUsed,
            remainingAmount = remainingAmount
        )
    }
}
