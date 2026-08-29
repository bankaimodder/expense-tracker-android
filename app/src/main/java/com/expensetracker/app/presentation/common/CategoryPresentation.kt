package com.expensetracker.app.presentation.common

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
import com.expensetracker.app.domain.model.Category

val Category.icon: ImageVector
    get() = when (this) {
        Category.FOOD -> Icons.Filled.Restaurant
        Category.TRANSPORT -> Icons.Filled.DirectionsBus
        Category.SHOPPING -> Icons.Filled.ShoppingBag
        Category.ENTERTAINMENT -> Icons.Filled.Movie
        Category.BILLS -> Icons.Filled.Receipt
        Category.SALARY -> Icons.Filled.AttachMoney
        Category.OTHER -> Icons.Filled.MoreHoriz
    }

val Category.color: Color
    get() = when (this) {
        Category.FOOD -> Color(0xFFFF7043)
        Category.TRANSPORT -> Color(0xFF42A5F5)
        Category.SHOPPING -> Color(0xFFAB47BC)
        Category.ENTERTAINMENT -> Color(0xFFEC407A)
        Category.BILLS -> Color(0xFFFFA726)
        Category.SALARY -> Color(0xFF66BB6A)
        Category.OTHER -> Color(0xFF78909C)
    }
