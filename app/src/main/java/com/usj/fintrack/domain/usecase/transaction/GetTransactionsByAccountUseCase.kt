package com.usj.fintrack.domain.usecase.transaction

import com.usj.fintrack.domain.model.Transaction
import com.usj.fintrack.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTransactionsByAccountUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    operator fun invoke(accountId: Long): Flow<List<Transaction>> =
        transactionRepository.getTransactionsByAccount(accountId)
}
