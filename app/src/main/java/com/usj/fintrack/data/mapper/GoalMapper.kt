package com.usj.fintrack.data.mapper

import com.usj.fintrack.data.entity.GoalEntity
import com.usj.fintrack.domain.model.Goal

// ─── Entity → Domain ─────────────────────────────────────────────────────────

fun GoalEntity.toDomain(): Goal = Goal(
    id = goalId,
    userId = userId,
    name = name,
    targetAmount = targetAmount,
    currentAmount = currentAmount,
    deadline = deadline,
    isCompleted = isCompleted
)

// ─── Domain → Entity ─────────────────────────────────────────────────────────

fun Goal.toEntity(): GoalEntity = GoalEntity(
    goalId = id,
    userId = userId,
    name = name,
    targetAmount = targetAmount,
    currentAmount = currentAmount,
    deadline = deadline,
    isCompleted = isCompleted
)
