package com.usj.fintrack.domain.model.enum

/**
 * Indicates whether a [Transaction] represents money coming in or going out.
 *
 * - [INCOME]  — Funds received (salary, transfer in, refund, etc.).
 * - [EXPENSE] — Funds spent (purchase, payment, withdrawal, etc.).
 */
enum class TransactionType {
    INCOME,
    EXPENSE
}
