package com.expensetracker.app.presentation.statistics

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.expensetracker.app.domain.model.CategoryBreakdown
import com.expensetracker.app.presentation.common.color

@Composable
fun PieChart(
    data: List<CategoryBreakdown>,
    modifier: Modifier = Modifier
) {
    val total = data.sumOf { it.total }

    Canvas(modifier = modifier.size(200.dp)) {
        if (total <= 0.0) return@Canvas

        val strokeWidth = size.minDimension * 0.28f
        val diameter = size.minDimension - strokeWidth
        val topLeft = Offset(
            (size.width - diameter) / 2f,
            (size.height - diameter) / 2f
        )
        val arcSize = Size(diameter, diameter)

        var startAngle = -90f
        data.forEach { breakdown ->
            val sweep = (breakdown.total / total * 360f).toFloat()
            drawArc(
                color = breakdown.category.color,
                startAngle = startAngle,
                sweepAngle = sweep,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth)
            )
            startAngle += sweep
        }
    }
}
