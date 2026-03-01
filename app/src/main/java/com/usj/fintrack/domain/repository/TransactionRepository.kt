package com.usj.fintrack.domain.repository

import com.usj.fintrack.domain.model.Transaction
import com.usj.fintrack.domain.model.TransactionStats
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    fun getAllTransactions(): Flow<List<Transaction>>
    fun getTransactionsByAccount(accountId: Long): Flow<List<Transaction>>
    suspend fun getTransactionById(id: Long): Transaction?
    suspend fun createTransaction(transaction: Transaction): Result<Long>
    suspend fun updateTransaction(transaction: Transaction): Result<Unit>
    suspend fun deleteTransaction(transactionId: Long): Result<Unit>
    suspend fun getTransactionStats(startDate: Long, endDate: Long): TransactionStats
}
