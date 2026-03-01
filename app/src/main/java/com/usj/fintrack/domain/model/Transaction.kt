package com.usj.fintrack.domain.model

import com.usj.fintrack.domain.model.enum.TransactionType

/**
 * Domain model for a single financial transaction.
 *
 * @property id          Local auto-generated primary key.
 * @property accountId   Foreign key referencing [Account.id].
 * @property amount      Absolute monetary value (always positive; direction
 *                       is determined by [type]).
 * @property type        Whether this is income or an expense — see [TransactionType].
 * @property date        Unix epoch milliseconds of the transaction date.
 * @property description Short user-provided or ML-extracted description.
 * @property imageUri    Optional URI of a captured ticket photo.
 * @property location    Optional merchant or location string.
 * @property categories  Categories assigned to this transaction (N-N).
 */
data class Transaction(
    val id: Long = 0,
    val accountId: Long,
    val amount: Double,
    val type: TransactionType,
    val date: Long,
    val description: String,
    val imageUri: String? = null,
    val location: String? = null,
    val categories: List<Category> = emptyList()
)
