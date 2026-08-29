package com.expensetracker.app.presentation.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.expensetracker.app.presentation.common.EmptyState
import com.expensetracker.app.presentation.common.ErrorState
import com.expensetracker.app.presentation.common.LoadingState
import com.expensetracker.app.presentation.common.formatAmount
import com.expensetracker.app.presentation.theme.ExpenseRed
import com.expensetracker.app.presentation.theme.IncomeGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    viewModel: StatisticsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Statistics") }) }
    ) { paddingValues ->
        when {
            uiState.isLoading -> LoadingState(modifier = Modifier.padding(paddingValues))
            uiState.errorMessage != null -> ErrorState(
                message = uiState.errorMessage ?: "Something went wrong",
                modifier = Modifier.padding(paddingValues)
            )
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    item {
                        Text(
                            text = "Spending by Category",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    item {
                        if (uiState.categoryBreakdown.isEmpty()) {
                            EmptyState(message = "No expenses recorded this month yet.")
                        } else {
                            CategoryBreakdownSection(uiState = uiState)
                        }
                    }
                    item {
                        Text(
                            text = "Monthly Overview",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    item {
                        Card {
                            Column(modifier = Modifier.padding(16.dp)) {
                                MonthlyBarChart(data = uiState.monthlySummary)
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 12.dp),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    LegendDot(color = IncomeGreen, label = "Income")
                                    Box(modifier = Modifier.size(16.dp))
                                    LegendDot(color = ExpenseRed, label = "Expense")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryBreakdownSection(uiState: StatisticsUiState) {
    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PieChart(data = uiState.categoryBreakdown)
            Text(
                text = "Total spent: ${formatAmount(uiState.totalExpenseThisMonth, uiState.currency)}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 12.dp)
            )

            uiState.categoryBreakdown.forEach { breakdown ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(breakdown.category.color)
                    )
                    Text(
                        text = breakdown.category.displayName,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .weight(1f)
                    )
                    Text(
                        text = "${"%.1f".format(breakdown.percentage)}%",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                    Text(
                        text = formatAmount(breakdown.total, uiState.currency),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun LegendDot(color: androidx.compose.ui.graphics.Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(color)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(start = 6.dp, end = 12.dp)
        )
    }
}
