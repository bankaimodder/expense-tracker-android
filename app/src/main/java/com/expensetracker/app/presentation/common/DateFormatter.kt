package com.expensetracker.app.presentation.common

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

private val displayFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.US)

fun LocalDate.toDisplayString(): String = format(displayFormatter)

fun java.time.YearMonth.toDisplayLabel(): String =
    "${month.getDisplayName(TextStyle.SHORT, Locale.US)} '${(year % 100).toString().padStart(2, '0')}"
