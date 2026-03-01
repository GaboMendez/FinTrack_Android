package com.usj.fintrack.domain.usecase.settings

import com.usj.fintrack.domain.model.enum.Currency
import com.usj.fintrack.domain.repository.UserPreferencesRepository
import javax.inject.Inject

class UpdateCurrencyUseCase @Inject constructor(
    private val repository: UserPreferencesRepository
) {
    suspend operator fun invoke(currency: Currency) = repository.updateCurrency(currency)
}
