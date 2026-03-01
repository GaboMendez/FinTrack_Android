package com.usj.fintrack.domain.usecase.transaction

import com.usj.fintrack.domain.model.Transaction
import com.usj.fintrack.domain.repository.TransactionRepository
import javax.inject.Inject

class GetTransactionByIdUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(id: Long): Transaction? =
        transactionRepository.getTransactionById(id)
}
