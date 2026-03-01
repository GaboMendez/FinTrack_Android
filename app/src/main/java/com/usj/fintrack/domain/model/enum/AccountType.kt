package com.usj.fintrack.domain.model.enum

/**
 * Classifies the nature of a financial account.
 *
 * - [CHECKING]     — Standard bank current/checking account.
 * - [SAVINGS]      — Savings or deposit account.
 * - [CASH]         — Physical cash wallet tracked manually.
 * - [CREDIT_CARD]  — Credit-card account (balance may be negative / debt).
 */
enum class AccountType {
    CHECKING,
    SAVINGS,
    CASH,
    CREDIT_CARD
}
