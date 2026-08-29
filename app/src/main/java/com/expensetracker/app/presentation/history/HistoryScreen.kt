package com.expensetracker.app.presentation.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.expensetracker.app.domain.model.Category
import com.expensetracker.app.domain.model.Transaction
import com.expensetracker.app.presentation.common.EmptyState
import com.expensetracker.app.presentation.common.ErrorState
import com.expensetracker.app.presentation.common.TransactionListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var transactionPendingDelete by remember { mutableStateOf<Transaction?>(null) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Transaction History") }) }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = viewModel::onSearchQueryChanged,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("Search by note") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                trailingIcon = {
                    if (uiState.searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.onSearchQueryChanged("") }) {
                            Icon(Icons.Filled.Close, contentDescription = "Clear")
                        }
                    }
                },
                singleLine = true
            )

            CategoryFilterRow(
                selectedCategory = uiState.selectedCategory,
                onCategorySelected = viewModel::onCategorySelected
            )

            HorizontalDivider()

            when {
                uiState.errorMessage != null -> ErrorState(message = uiState.errorMessage!!)
                uiState.transactions.isEmpty() -> EmptyState(message = "No transactions match your filters.")
                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        items(uiState.transactions, key = { it.id }) { transaction ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                TransactionListItem(
                                    transaction = transaction,
                                    currency = uiState.currency,
                                    modifier = Modifier.weight(1f)
                                )
                                IconButton(onClick = { transactionPendingDelete = transaction }) {
                                    Icon(
                                        imageVector = Icons.Filled.Delete,
                                        contentDescription = "Delete",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    }

    transactionPendingDelete?.let { transaction ->
        AlertDialog(
            onDismissRequest = { transactionPendingDelete = null },
            title = { Text("Delete transaction?") },
            text = { Text("This will permanently delete this ${transaction.category.displayName.lowercase()} transaction.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.onDeleteTransaction(transaction)
                    transactionPendingDelete = null
                }) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { transactionPendingDelete = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun CategoryFilterRow(
    selectedCategory: Category?,
    onCategorySelected: (Category?) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
    ) {
        item {
            FilterChip(
                selected = selectedCategory == null,
                onClick = { onCategorySelected(null) },
                label = { Text("All") }
            )
        }
        items(Category.entries.toList()) { category ->
            FilterChip(
                selected = selectedCategory == category,
                onClick = { onCategorySelected(if (selectedCategory == category) null else category) },
                label = { Text(category.displayName) }
            )
        }
    }
}
