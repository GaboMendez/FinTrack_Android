package com.usj.fintrack.domain.usecase.transaction

import com.usj.fintrack.domain.model.Transaction
import com.usj.fintrack.domain.model.enum.TransactionType
import com.usj.fintrack.domain.repository.AccountRepository
import com.usj.fintrack.domain.repository.TransactionRepository
import javax.inject.Inject

class UpdateTransactionUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(transaction: Transaction): Result<Unit> {
        if (transaction.amount <= 0) {
            return Result.failure(IllegalArgumentException("Amount must be positive"))
        }
        if (transaction.description.isBlank()) {
            return Result.failure(IllegalArgumentException("Description must not be blank"))
        }

        // Reverse old balance impact, then apply new one
        val old = transactionRepository.getTransactionById(transaction.id)
        val account = accountRepository.getAccountById(transaction.accountId)
            ?: return Result.failure(IllegalArgumentException("Account not found"))

        val result = transactionRepository.updateTransaction(transaction)

        if (result.isSuccess && old != null) {
            val reversedBalance = when (old.type) {
                TransactionType.INCOME -> account.balance - old.amount
                TransactionType.EXPENSE -> account.balance + old.amount
            }
            val newBalance = when (transaction.type) {
                TransactionType.INCOME -> reversedBalance + transaction.amount
                TransactionType.EXPENSE -> reversedBalance - transaction.amount
            }
            accountRepository.updateAccountBalance(account.id, newBalance)
        }

        return result
    }
}
