package com.expensetracker.app.domain.model

enum class Category(val displayName: String) {
    FOOD("Food"),
    TRANSPORT("Transport"),
    SHOPPING("Shopping"),
    ENTERTAINMENT("Entertainment"),
    BILLS("Bills"),
    SALARY("Salary"),
    OTHER("Other");

    companion object {
        val expenseCategories = listOf(FOOD, TRANSPORT, SHOPPING, ENTERTAINMENT, BILLS, OTHER)
        val incomeCategories = listOf(SALARY, OTHER)

        fun categoriesFor(type: TransactionType): List<Category> =
            if (type == TransactionType.INCOME) incomeCategories else expenseCategories
    }
}
