package com.example.budget.viewModel.recyclerView

import androidx.lifecycle.ViewModel
import com.example.budget.adapters.recyclerView.OperationType
import com.example.budget.fragments.bottomSheetDialogFragment.Direction
import com.example.budget.repository.FormatterRepository.dayWithMonth
import com.example.budget.viewModel.wrap.FieldWrapWithError
import com.example.budget.viewModel.wrap.FieldWrap
import java.util.*

class FiltersViewModel : ViewModel() {
    val operationType: FieldWrap<OperationType?> = FieldWrapWithError()

    val sortBy: FieldWrap<Direction?> = FieldWrapWithError()

    val dateRange: FieldWrap<Pair<Date, Date>> = FieldWrapWithError()

    fun dateRangeToString(): String? =
        dateRange.data.value?.run {
            val format = { d: Date -> dayWithMonth.format(d) }
            val (s, e) = this.map(format)
            return "$s – $e"
        }

    val sumRange: FieldWrap<Pair<Float, Float>> = FieldWrapWithError()

    fun sumRangeToString(): String? =
        sumRange.data.value?.run {
            val (f, s) = this.map{ it.toInt() }
            return "$f .. $s"
        }

    private fun <T, K> Pair<T, T>.map(transform: (T) -> K): Pair<K, K> = transform(first) to transform(second)


    fun getFilters(): Filters = Filters(
            type = operationType.getOrNull(),
            dateRange = dateRange.getOrNull(),
            sumRange = sumRange.getOrNull(),
            direction = sortBy.getOrNull()
        )
}

data class Filters(
    var type: OperationType? = null,
    var categoryId: Int? = null,
    var dateRange: Pair<Date?, Date?>? = null to null,
    var sumRange: Pair<Float?, Float?>? = null to null,
    var direction: Direction? = null,
)