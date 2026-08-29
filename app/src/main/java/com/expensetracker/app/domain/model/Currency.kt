package com.expensetracker.app.domain.model

enum class Currency(val code: String, val symbol: String, val displayName: String) {
    USD("USD", "$", "US Dollar"),
    EUR("EUR", "€", "Euro"),
    GBP("GBP", "£", "British Pound"),
    INR("INR", "₹", "Indian Rupee"),
    JPY("JPY", "¥", "Japanese Yen");

    companion object {
        fun fromCode(code: String): Currency = entries.find { it.code == code } ?: USD
    }
}
