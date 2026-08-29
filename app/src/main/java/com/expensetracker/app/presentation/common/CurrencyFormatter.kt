package com.expensetracker.app.presentation.common

import com.expensetracker.app.domain.model.Currency
import java.util.Locale

fun formatAmount(amount: Double, currency: Currency): String {
    return "${currency.symbol}${String.format(Locale.US, "%,.2f", amount)}"
}
