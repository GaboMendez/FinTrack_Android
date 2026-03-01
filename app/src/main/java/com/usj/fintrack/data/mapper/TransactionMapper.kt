package com.usj.fintrack.data.mapper

import com.usj.fintrack.data.entity.TransactionEntity
import com.usj.fintrack.domain.model.Category
import com.usj.fintrack.domain.model.Transaction
import com.usj.fintrack.domain.model.enum.TransactionType

// ─── Entity → Domain ─────────────────────────────────────────────────────────

/**
 * Maps a [TransactionEntity] to the [Transaction] domain model.
 *
 * @param categories Pre-resolved list of [Category] domain models for this
 *                   transaction (loaded via [TransactionWithCategories] relation).
 *                   Defaults to an empty list when the caller only needs basic
 *                   transaction data without the N-N category join.
 */
fun TransactionEntity.toDomain(categories: List<Category> = emptyList()): Transaction = Transaction(
    id = transactionId,
    accountId = accountId,
    amount = amount,
    type = TransactionType.valueOf(type),
    date = date,
    description = description,
    imageUri = imageUri,
    location = location,
    categories = categories
)

// ─── Domain → Entity ─────────────────────────────────────────────────────────

/**
 * Maps a [Transaction] domain model to [TransactionEntity] for Room persistence.
 *
 * `createdAt` is stamped with the current system time on every call, so this
 * function is intended for **insert** operations. For update operations the
 * repository implementation should either preserve the original `createdAt` by
 * reading it from the existing entity beforehand, or rely on Room's `@Update`.
 */
fun Transaction.toEntity(): TransactionEntity = TransactionEntity(
    transactionId = id,
    accountId = accountId,
    amount = amount,
    type = type.name,
    date = date,
    description = description,
    imageUri = imageUri,
    location = location,
    createdAt = System.currentTimeMillis()
)
