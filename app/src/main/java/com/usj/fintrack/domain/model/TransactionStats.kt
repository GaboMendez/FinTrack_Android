package com.usj.fintrack.domain.model

/**
 * Aggregated income/expense statistics for a given time range.
 *
 * Produced by [GetTransactionStatsUseCase] and consumed by the Dashboard
 * and Transactions ViewModels.
 *
 * @property totalIncome      Sum of all [TransactionType.INCOME] amounts.
 * @property totalExpense     Sum of all [TransactionType.EXPENSE] amounts.
 * @property balance          [totalIncome] - [totalExpense].
 * @property transactionCount Total number of transactions in the range.
 */
data class TransactionStats(
    val totalIncome: Double = 0.0,
    val totalExpense: Double = 0.0,
    val balance: Double = totalIncome - totalExpense,
    val transactionCount: Int = 0
)
