package com.usj.fintrack.domain.usecase.account

import com.usj.fintrack.domain.model.Account
import com.usj.fintrack.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllAccountsUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {
    operator fun invoke(): Flow<List<Account>> = accountRepository.getAllAccounts()
}
