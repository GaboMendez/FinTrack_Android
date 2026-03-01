package com.usj.fintrack.domain.model

import com.usj.fintrack.domain.model.enum.BudgetStatusType

/**
 * A computed view of a [Budget]'s current state, used by the UI layer
 * to render progress bars and alert badges without extra calculations.
 *
 * Produced by [CheckBudgetStatusUseCase].
 *
 * @property budget                The underlying [Budget] domain model.
 * @property percentageUsed        Ratio of computed spend to [Budget.limitAmount]; > 1.0 = exceeded.
 * @property remainingAmount       [Budget.limitAmount] - [computedSpentAmount]; negative when exceeded.
 * @property computedSpentAmount   Actual spend derived from real transactions (not the stored field).
 * @property computedStatusType    Dynamic status derived from [percentageUsed].
 */
data class BudgetStatus(
    val budget: Budget,
    val percentageUsed: Double,
    val remainingAmount: Double,
    val computedSpentAmount: Double,
    val computedStatusType: BudgetStatusType
)
