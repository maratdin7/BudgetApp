package com.example.budget.repository.view

import android.app.DatePickerDialog
import android.content.Context
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.*

class DateField(private val formatter: SimpleDateFormat) {

    private fun Calendar.toStringFormat(): String = formatter.format(this.time)

    fun createDatePickerDialog(
        context: Context,
        textInputLayout: TextInputLayout,
        editDateCallback: (dateStr: String) -> Unit,
    ) {
        val calendar = Calendar.getInstance()
        editDateCallback(calendar.toStringFormat())

        textInputLayout.setEndIconOnClickListener { setOnClickListener(context, calendar, editDateCallback) }
        textInputLayout.errorIconDrawable = null
    }

    private fun setOnClickListener(
        context: Context,
        calendar: Calendar,
        editDateCallback: (dateStr: String) -> Unit,
    ) {
        calendar.firstDayOfWeek = Calendar.MONDAY
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)

        val onDateSetListener = DatePickerDialog.OnDateSetListener { _, yyyy, mm, dd ->
            calendar.set(yyyy, mm, dd)
            editDateCallback(calendar.toStringFormat())
        }

        DatePickerDialog(
            context,
            onDateSetListener,
            year,
            month,
            day
        ).show()
    }
}