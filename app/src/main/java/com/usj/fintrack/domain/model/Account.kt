package com.usj.fintrack.domain.model

import com.usj.fintrack.domain.model.enum.AccountType
import com.usj.fintrack.domain.model.enum.Currency

/**
 * Domain model representing a financial account owned by a user.
 *
 * @property id         Local auto-generated primary key.
 * @property userId     Foreign key referencing [User.id].
 * @property name       Human-readable account name (e.g. "Santander Nómina").
 * @property type       Classifies the account — see [AccountType].
 * @property balance    Current balance; may be negative for credit cards.
 * @property currency   Currency of this account — see [Currency].
 * @property createdAt  Unix epoch milliseconds when the account was created.
 */
data class Account(
    val id: Long = 0,
    val userId: Long,
    val name: String,
    val type: AccountType,
    val balance: Double = 0.0,
    val currency: Currency = Currency.EUR,
    val createdAt: Long
)
