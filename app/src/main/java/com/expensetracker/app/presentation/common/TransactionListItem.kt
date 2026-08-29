package com.expensetracker.app.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.expensetracker.app.domain.model.Currency
import com.expensetracker.app.domain.model.Transaction
import com.expensetracker.app.domain.model.TransactionType

@Composable
fun TransactionListItem(
    transaction: Transaction,
    currency: Currency,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(transaction.category.color.copy(alpha = 0.15f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = transaction.category.icon,
                contentDescription = transaction.category.displayName,
                tint = transaction.category.color
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = transaction.category.displayName,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = transaction.note.ifBlank { transaction.date.toDisplayString() },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )
        }

        val amountPrefix = if (transaction.type == TransactionType.INCOME) "+" else "-"
        val amountColor = if (transaction.type == TransactionType.INCOME) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.error
        }

        Text(
            text = "$amountPrefix${formatAmount(transaction.amount, currency)}",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = amountColor
        )
    }
}
