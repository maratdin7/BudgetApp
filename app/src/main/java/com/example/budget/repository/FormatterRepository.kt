package com.example.budget.repository

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

object FormatterRepository {

    val priceFormatter: NumberFormat = NumberFormat.getCurrencyInstance(Locale("ru", "RU"))

    fun priceFormatter(maxDigits: Int) = priceFormatter.apply { maximumFractionDigits = maxDigits }

    val fullDateFormatter = createDateFormat("dd/MM/yyyy")
    val sqlDateFormatter = createDateFormat("yyyy-MM-dd")
    val dayWithMonth = createDateFormat("d MMM")
    val dayWithFullMonth = createDateFormat("d MMMM")
    val onlyDayFormatter = createDateFormat("d")

    fun onlyDayFormatter(str: String) = createDateFormat("d $str")

    private fun createDateFormat(pattern: String): SimpleDateFormat =
        SimpleDateFormat(pattern, Locale.getDefault())

}