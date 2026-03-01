package com.usj.fintrack.domain.usecase.account

import com.usj.fintrack.domain.model.Account
import com.usj.fintrack.domain.repository.AccountRepository
import javax.inject.Inject

class GetAccountByIdUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(id: Long): Account? = accountRepository.getAccountById(id)
}
