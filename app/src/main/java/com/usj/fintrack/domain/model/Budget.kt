package com.usj.fintrack.domain.model

import com.usj.fintrack.domain.model.enum.BudgetStatusType

/**
 * Domain model for a spending budget tied to a category and time period.
 *
 * @property id              Local auto-generated primary key.
 * @property userId          Foreign key referencing [User.id].
 * @property categoryId      Foreign key referencing [Category.id].
 * @property limitAmount     Maximum amount the user wants to spend in the period.
 * @property spentAmount     Amount already spent (updated as transactions are added).
 * @property periodStartDate Unix epoch milliseconds for the start of the period.
 * @property periodEndDate   Unix epoch milliseconds for the end of the period.
 * @property status          Current health of the budget — see [BudgetStatusType].
 */
data class Budget(
    val id: Long = 0,
    val userId: Long,
    val categoryId: Long,
    val limitAmount: Double,
    val spentAmount: Double = 0.0,
    val periodStartDate: Long,
    val periodEndDate: Long,
    val status: BudgetStatusType = BudgetStatusType.ON_TRACK
)
