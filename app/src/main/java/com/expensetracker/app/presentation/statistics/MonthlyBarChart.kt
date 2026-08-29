package com.expensetracker.app.presentation.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.expensetracker.app.domain.model.MonthlySummary
import com.expensetracker.app.presentation.common.toDisplayLabel
import com.expensetracker.app.presentation.theme.ExpenseRed
import com.expensetracker.app.presentation.theme.IncomeGreen

@Composable
fun MonthlyBarChart(
    data: List<MonthlySummary>,
    modifier: Modifier = Modifier
) {
    val maxValue = (data.maxOfOrNull { maxOf(it.income, it.expense) } ?: 0.0).coerceAtLeast(1.0)
    val chartHeight = 160.dp

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(chartHeight + 32.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        data.forEach { summary ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier.fillMaxHeight()
            ) {
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(3.dp),
                    modifier = Modifier.height(chartHeight)
                ) {
                    Box(
                        modifier = Modifier
                            .width(10.dp)
                            .fillMaxHeight(fraction = (summary.income / maxValue).toFloat().coerceIn(0.02f, 1f))
                            .clip(RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp))
                            .background(IncomeGreen)
                    )
                    Box(
                        modifier = Modifier
                            .width(10.dp)
                            .fillMaxHeight(fraction = (summary.expense / maxValue).toFloat().coerceIn(0.02f, 1f))
                            .clip(RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp))
                            .background(ExpenseRed)
                    )
                }
                Text(
                    text = summary.yearMonth.toDisplayLabel(),
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}
