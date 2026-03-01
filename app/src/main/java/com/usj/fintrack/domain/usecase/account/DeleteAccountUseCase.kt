package com.usj.fintrack.domain.usecase.account

import com.usj.fintrack.domain.repository.AccountRepository
import javax.inject.Inject

class DeleteAccountUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(id: Long): Result<Unit> =
        runCatching { accountRepository.deleteAccount(id) }
}
