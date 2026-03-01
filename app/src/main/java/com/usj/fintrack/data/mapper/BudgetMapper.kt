package com.usj.fintrack.data.mapper

import com.usj.fintrack.data.entity.BudgetEntity
import com.usj.fintrack.domain.model.Budget
import com.usj.fintrack.domain.model.enum.BudgetStatusType

// ─── Entity → Domain ─────────────────────────────────────────────────────────

fun BudgetEntity.toDomain(): Budget = Budget(
    id = budgetId,
    userId = userId,
    categoryId = categoryId,
    limitAmount = limitAmount,
    spentAmount = spentAmount,
    periodStartDate = periodStartDate,
    periodEndDate = periodEndDate,
    status = BudgetStatusType.valueOf(status)
)

// ─── Domain → Entity ─────────────────────────────────────────────────────────

fun Budget.toEntity(): BudgetEntity = BudgetEntity(
    budgetId = id,
    userId = userId,
    categoryId = categoryId,
    limitAmount = limitAmount,
    spentAmount = spentAmount,
    periodStartDate = periodStartDate,
    periodEndDate = periodEndDate,
    status = status.name
)
