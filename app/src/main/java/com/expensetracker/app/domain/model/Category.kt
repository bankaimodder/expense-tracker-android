package com.expensetracker.app.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

enum class Category(
    val displayName: String,
    val icon: ImageVector,
    val color: Color
) {
    FOOD("Food", Icons.Filled.Restaurant, Color(0xFFFF7043)),
    TRANSPORT("Transport", Icons.Filled.DirectionsBus, Color(0xFF42A5F5)),
    SHOPPING("Shopping", Icons.Filled.ShoppingBag, Color(0xFFAB47BC)),
    ENTERTAINMENT("Entertainment", Icons.Filled.Movie, Color(0xFFEC407A)),
    BILLS("Bills", Icons.Filled.Receipt, Color(0xFFFFA726)),
    SALARY("Salary", Icons.Filled.AttachMoney, Color(0xFF66BB6A)),
    OTHER("Other", Icons.Filled.MoreHoriz, Color(0xFF78909C));

    companion object {
        val expenseCategories = listOf(FOOD, TRANSPORT, SHOPPING, ENTERTAINMENT, BILLS, OTHER)
        val incomeCategories = listOf(SALARY, OTHER)

        fun categoriesFor(type: TransactionType): List<Category> =
            if (type == TransactionType.INCOME) incomeCategories else expenseCategories
    }
}
