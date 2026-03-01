package com.usj.fintrack.domain.usecase.transaction

import com.usj.fintrack.domain.model.Transaction
import com.usj.fintrack.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTransactionsUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    operator fun invoke(): Flow<List<Transaction>> = transactionRepository.getAllTransactions()
}
