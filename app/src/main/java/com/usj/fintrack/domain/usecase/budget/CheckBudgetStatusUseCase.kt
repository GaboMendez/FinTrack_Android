package com.usj.fintrack.domain.usecase.budget

import com.usj.fintrack.domain.model.Budget
import com.usj.fintrack.domain.model.BudgetStatus
import com.usj.fintrack.domain.model.Transaction
import com.usj.fintrack.domain.model.enum.BudgetStatusType
import com.usj.fintrack.domain.model.enum.TransactionType
import javax.inject.Inject

class CheckBudgetStatusUseCase @Inject constructor() {

    /**
     * Compute the real budget status from [transactions].
     * Only EXPENSE transactions whose category matches [Budget.categoryId]
     * and whose date falls within the budget period are counted.
     */
    operator fun invoke(budget: Budget, transactions: List<Transaction>): BudgetStatus {
        val spentAmount = transactions
            .filter { tx ->
                tx.type == TransactionType.EXPENSE &&
                tx.categories.any { it.id == budget.categoryId } &&
                tx.date in budget.periodStartDate..budget.periodEndDate
            }
            .sumOf { it.amount }

        val percentageUsed = if (budget.limitAmount > 0) spentAmount / budget.limitAmount else 0.0
        val remainingAmount = budget.limitAmount - spentAmount
        val statusType = when {
            percentageUsed < 0.8 -> BudgetStatusType.ON_TRACK
            percentageUsed < 1.0 -> BudgetStatusType.WARNING
            else                 -> BudgetStatusType.EXCEEDED
        }

        return BudgetStatus(
            budget = budget,
            percentageUsed = percentageUsed,
            remainingAmount = remainingAmount,
            computedSpentAmount = spentAmount,
            computedStatusType = statusType
        )
    }
}
