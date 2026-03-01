package com.usj.fintrack.domain.model

/**
 * A computed view of a [Budget]'s current state, used by the UI layer
 * to render progress bars and alert badges without extra calculations.
 *
 * Produced by [CheckBudgetStatusUseCase].
 *
 * @property budget           The underlying [Budget] domain model.
 * @property percentageUsed   Ratio of [Budget.spentAmount] to [Budget.limitAmount]
 *                            expressed as a value in [0.0, ∞) — values > 1.0 mean
 *                            the budget is exceeded.
 * @property remainingAmount  [Budget.limitAmount] - [Budget.spentAmount]; negative
 *                            when the budget is exceeded.
 */
data class BudgetStatus(
    val budget: Budget,
    val percentageUsed: Double,
    val remainingAmount: Double
)
