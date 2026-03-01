package com.usj.fintrack.data.mapper

import com.usj.fintrack.data.entity.UserEntity
import com.usj.fintrack.domain.model.User

// ─── Entity → Domain ─────────────────────────────────────────────────────────

fun UserEntity.toDomain(): User = User(
    id = userId,
    email = email,
    createdAt = createdAt
)

// ─── Domain → Entity ─────────────────────────────────────────────────────────

fun User.toEntity(): UserEntity = UserEntity(
    userId = id,
    email = email,
    createdAt = createdAt
)
