package com.usj.fintrack.domain.usecase.transaction

import com.usj.fintrack.domain.model.Transaction
import com.usj.fintrack.domain.model.enum.TransactionType
import com.usj.fintrack.domain.repository.AccountRepository
import com.usj.fintrack.domain.repository.TransactionRepository
import javax.inject.Inject

class CreateTransactionUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(transaction: Transaction): Result<Long> {
        if (transaction.amount <= 0) {
            return Result.failure(IllegalArgumentException("Amount must be positive"))
        }
        if (transaction.description.isBlank()) {
            return Result.failure(IllegalArgumentException("Description must not be blank"))
        }

        val account = accountRepository.getAccountById(transaction.accountId)
            ?: return Result.failure(IllegalArgumentException("Account not found"))

        val result = transactionRepository.createTransaction(transaction)

        if (result.isSuccess) {
            val newBalance = when (transaction.type) {
                TransactionType.INCOME -> account.balance + transaction.amount
                TransactionType.EXPENSE -> account.balance - transaction.amount
            }
            accountRepository.updateAccountBalance(account.id, newBalance)
        }

        return result
    }
}
