package com.example.budget.adapters.recyclerView.withFilters

import androidx.fragment.app.FragmentManager
import com.example.budget.R
import com.example.budget.adapters.recyclerView.OperationType
import com.example.budget.fragments.bottomSheetDialogFragment.Direction
import com.example.budget.fragments.bottomSheetDialogFragment.OperationTypeBottomSheet
import com.example.budget.fragments.bottomSheetDialogFragment.SortByBottomSheet
import com.example.budget.fragments.bottomSheetDialogFragment.SumRangeBottomSheet
import com.example.budget.viewModel.wrap.FieldWrap
import com.example.budget.viewModel.wrap.FieldWrapWithError
import com.google.android.material.chip.Chip
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.*

class Filters(private val parentFragmentManager: FragmentManager) {
    fun createDateRangeFilter(dateRange: Chip, dateRangeField: FieldWrap<Pair<Date, Date>>) {
        dateRange.setOnClickListener {
            createDatePickerRange(dateRangeField)
        }

        dateRange.clearOnCloseIconClickListener(dateRangeField)
    }

    private fun createDatePickerRange(dateRangeField: FieldWrap<Pair<Date, Date>>) {
        val now = Calendar.getInstance().timeInMillis

        val curRange = dateRangeField.data.value.run {
            if (this != null)
                PairX(first.time, second.time)
            else PairX(now, now)
        }

        val datePickerRange = MaterialDatePicker.Builder.dateRangePicker().apply {
            setSelection(curRange)
            setTheme(R.style.ThemeOverlay_MaterialComponents_MaterialCalendar)
        }

        val picker = datePickerRange.build()
        picker.show(parentFragmentManager, picker.toString())

        picker.addOnNegativeButtonClickListener { picker.dismiss() }
        picker.addOnPositiveButtonClickListener {
            val start = Date(it.first!!)
            val end = Date(it.second!!)
            dateRangeField.setValue(start to end)
        }
    }

    fun createSumRangeFilter(sumRange: Chip, sumRangeField: FieldWrap<Pair<Float, Float>>) {
        val sumRangeBottomSheet = SumRangeBottomSheet(sumRangeField)
        sumRange.setOnClickListener {
            sumRangeBottomSheet.show(parentFragmentManager, SumRangeBottomSheet.TAG)
        }

        sumRange.clearOnCloseIconClickListener(sumRangeField)
    }

    fun createSortByFilter(sortBy: Chip, sortByField: FieldWrap<Direction?>) {
        val sortByBottomSheet = SortByBottomSheet(sortByField)
        sortBy.setOnClickListener {
            sortByBottomSheet.show(parentFragmentManager, SortByBottomSheet.TAG)
        }

        sortBy.clearOnCloseIconClickListener(sortByField)
    }

    fun createOperationTypeFilter(operationType: Chip, operationTypeField: FieldWrap<OperationType?>) {
        val operationTypeBottomSheet =
            OperationTypeBottomSheet(operationTypeField)
        operationType.setOnClickListener {
            operationTypeBottomSheet.show(parentFragmentManager, OperationTypeBottomSheet.TAG)
        }

        operationType.clearOnCloseIconClickListener(operationTypeField)
    }

    private fun Chip.clearOnCloseIconClickListener(field: FieldWrapWithError<out Any?, out Any?>) {
        setOnCloseIconClickListener { field.clear() }
    }
}