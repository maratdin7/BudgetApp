package com.example.budget.repository

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

object FormatterRepository {

    val priceFormatter: NumberFormat = NumberFormat.getCurrencyInstance(Locale("ru", "RU"))

    val fullDateFormatter = createDateFormat("dd/MM/yyyy")
    val sqlDateFormatter = createDateFormat("yyyy-MM-dd")
    val dayWithMonth = createDateFormat("d MMM")
    val dayWithFullMonth = createDateFormat("d MMMM")
    val onlyDayFormatter = createDateFormat("d")

    private fun createDateFormat(pattern: String): SimpleDateFormat =
        SimpleDateFormat(pattern, Locale.getDefault())

}