package com.usj.fintrack.domain.usecase.transaction

import com.usj.fintrack.domain.model.enum.TransactionType
import com.usj.fintrack.domain.repository.AccountRepository
import com.usj.fintrack.domain.repository.TransactionRepository
import javax.inject.Inject

class DeleteTransactionUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(transactionId: Long): Result<Unit> {
        val transaction = transactionRepository.getTransactionById(transactionId)
            ?: return Result.failure(IllegalArgumentException("Transaction not found"))

        val result = transactionRepository.deleteTransaction(transactionId)

        if (result.isSuccess) {
            val account = accountRepository.getAccountById(transaction.accountId)
            if (account != null) {
                // Reverse the balance impact of the deleted transaction
                val restoredBalance = when (transaction.type) {
                    TransactionType.INCOME -> account.balance - transaction.amount
                    TransactionType.EXPENSE -> account.balance + transaction.amount
                }
                accountRepository.updateAccountBalance(account.id, restoredBalance)
            }
        }

        return result
    }
}
