package com.usj.fintrack.domain.usecase.account

import com.usj.fintrack.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CalculateTotalBalanceUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {
    operator fun invoke(): Flow<Double> =
        accountRepository.getAllAccounts().map { accounts ->
            accounts.sumOf { it.balance }
        }
}
