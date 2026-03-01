package com.usj.fintrack.data.mapper

import com.usj.fintrack.data.entity.CategoryEntity
import com.usj.fintrack.domain.model.Category

// ─── Entity → Domain ─────────────────────────────────────────────────────────

fun CategoryEntity.toDomain(): Category = Category(
    id = categoryId,
    name = name,
    iconName = iconName,
    colorHex = colorHex,
    isDefault = isDefault
)

// ─── Domain → Entity ─────────────────────────────────────────────────────────

fun Category.toEntity(): CategoryEntity = CategoryEntity(
    categoryId = id,
    name = name,
    iconName = iconName,
    colorHex = colorHex,
    isDefault = isDefault
)
