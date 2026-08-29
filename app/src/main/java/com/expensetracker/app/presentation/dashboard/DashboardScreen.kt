package com.expensetracker.app.presentation.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.expensetracker.app.domain.model.Currency
import com.expensetracker.app.presentation.common.EmptyState
import com.expensetracker.app.presentation.common.ErrorState
import com.expensetracker.app.presentation.common.LoadingState
import com.expensetracker.app.presentation.common.TransactionListItem
import com.expensetracker.app.presentation.common.formatAmount
import com.expensetracker.app.presentation.theme.ExpenseTrackerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("ExpenseTracker") })
        }
    ) { paddingValues ->
        when {
            uiState.isLoading -> LoadingState(modifier = Modifier.padding(paddingValues))
            uiState.errorMessage != null -> ErrorState(
                message = uiState.errorMessage ?: "Something went wrong",
                modifier = Modifier.padding(paddingValues)
            )
            else -> DashboardContent(uiState = uiState, paddingValues = paddingValues)
        }
    }
}

@Composable
private fun DashboardContent(
    uiState: DashboardUiState,
    paddingValues: PaddingValues
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            BalanceCard(balance = uiState.totalBalance, currency = uiState.currency)
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SummaryCard(
                    modifier = Modifier.weight(1f),
                    title = "Monthly Income",
                    amount = uiState.monthlyIncome,
                    currency = uiState.currency,
                    color = MaterialTheme.colorScheme.primary
                )
                SummaryCard(
                    modifier = Modifier.weight(1f),
                    title = "Monthly Expenses",
                    amount = uiState.monthlyExpense,
                    currency = uiState.currency,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
        item {
            Text(
                text = "Recent Transactions",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
        }
        if (uiState.recentTransactions.isEmpty()) {
            item {
                EmptyState(message = "No transactions yet. Tap + to add your first one.")
            }
        } else {
            items(uiState.recentTransactions, key = { it.id }) { transaction ->
                Column {
                    TransactionListItem(transaction = transaction, currency = uiState.currency)
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
private fun BalanceCard(balance: Double, currency: Currency) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Total Balance",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = formatAmount(balance, currency),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
private fun SummaryCard(
    modifier: Modifier = Modifier,
    title: String,
    amount: Double,
    currency: Currency,
    color: androidx.compose.ui.graphics.Color
) {
    Card(modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = formatAmount(amount, currency),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = color
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DashboardContentPreview() {
    ExpenseTrackerTheme {
        DashboardContent(
            uiState = DashboardUiState(
                isLoading = false,
                totalBalance = 1250.75,
                monthlyIncome = 3000.0,
                monthlyExpense = 1749.25
            ),
            paddingValues = PaddingValues(0.dp)
        )
    }
}
