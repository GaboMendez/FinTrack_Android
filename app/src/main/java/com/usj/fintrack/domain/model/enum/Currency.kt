package com.usj.fintrack.domain.model.enum

/**
 * Supported fiat currencies with their display symbols.
 *
 * Add further entries here as the app expands to new markets.
 *
 * @property symbol The Unicode currency symbol used for formatting amounts.
 */
enum class Currency(val symbol: String) {
    USD("$"),
    EUR("€")
}
