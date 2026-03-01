package com.usj.fintrack.domain.usecase.transaction

import com.usj.fintrack.domain.model.TransactionStats
import com.usj.fintrack.domain.repository.TransactionRepository
import javax.inject.Inject

class GetTransactionStatsUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(startDate: Long, endDate: Long): TransactionStats =
        transactionRepository.getTransactionStats(startDate, endDate)
}
