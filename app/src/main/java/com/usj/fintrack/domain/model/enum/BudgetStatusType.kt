package com.usj.fintrack.domain.model.enum

/**
 * Represents the health state of a [Budget] at a given point in time.
 *
 * - [ON_TRACK] — Spending is within the safe portion of the budget limit.
 * - [WARNING]  — Spending is approaching the limit (e.g. >80 % consumed).
 * - [EXCEEDED] — Spending has surpassed the configured budget limit.
 */
enum class BudgetStatusType {
    ON_TRACK,
    WARNING,
    EXCEEDED
}
