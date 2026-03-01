package com.usj.fintrack.domain.usecase.account

import com.usj.fintrack.domain.model.Account
import com.usj.fintrack.domain.repository.AccountRepository
import javax.inject.Inject

class UpdateAccountUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(account: Account): Result<Unit> {
        if (account.name.isBlank()) {
            return Result.failure(IllegalArgumentException("Account name must not be blank"))
        }
        if (account.balance < 0) {
            return Result.failure(IllegalArgumentException("Balance must be >= 0"))
        }
        return runCatching { accountRepository.updateAccount(account) }
    }
}
