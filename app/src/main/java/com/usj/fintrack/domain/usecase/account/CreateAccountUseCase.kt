package com.usj.fintrack.domain.usecase.account

import com.usj.fintrack.domain.model.Account
import com.usj.fintrack.domain.repository.AccountRepository
import javax.inject.Inject

class CreateAccountUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(account: Account): Result<Long> {
        if (account.name.isBlank()) {
            return Result.failure(IllegalArgumentException("Account name must not be blank"))
        }
        if (account.balance < 0) {
            return Result.failure(IllegalArgumentException("Initial balance must be >= 0"))
        }
        return runCatching { accountRepository.createAccount(account) }
    }
}
