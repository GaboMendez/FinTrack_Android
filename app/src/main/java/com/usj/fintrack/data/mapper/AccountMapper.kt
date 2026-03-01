package com.usj.fintrack.data.mapper

import com.usj.fintrack.data.entity.AccountEntity
import com.usj.fintrack.domain.model.Account
import com.usj.fintrack.domain.model.enum.AccountType
import com.usj.fintrack.domain.model.enum.Currency

// ─── Entity → Domain ─────────────────────────────────────────────────────────

fun AccountEntity.toDomain(): Account = Account(
    id = accountId,
    userId = userId,
    name = name,
    type = AccountType.valueOf(type),
    balance = balance,
    currency = Currency.valueOf(currency),
    createdAt = createdAt
)

// ─── Domain → Entity ─────────────────────────────────────────────────────────

fun Account.toEntity(): AccountEntity = AccountEntity(
    accountId = id,
    userId = userId,
    name = name,
    type = type.name,
    balance = balance,
    currency = currency.name,
    createdAt = createdAt
)
