package com.example.budget.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.budget.repository.FormatterRepository.dayWithMonth
import java.text.SimpleDateFormat
import java.util.*

class HeaderItemFilterViewModel : ViewModel() {
    private val _operationType: MutableLiveData<String?> = MutableLiveData()
    val operationType: LiveData<String?> = _operationType

    fun setOperationType(type: String) {
        _operationType.value = type
    }

    private val _sortBy: MutableLiveData<String?> = MutableLiveData()
    val sortBy: LiveData<String?> = _sortBy

    fun setSortBy(type: String) {
        _sortBy.value = type
    }

    private val _dateRange: MutableLiveData<Pair<Date, Date>?> = MutableLiveData()
    val dateRange: LiveData<Pair<Date, Date>?> = _dateRange

    fun dateRangeToString(): String? {
        val dateRange = this.dateRange.value ?: return null
        val s = dayWithMonth.format(dateRange.first)
        val e = dayWithMonth.format(dateRange.second)
        return "$s - $e"
    }

    fun setDateRange(start: Date, end: Date) {
        _dateRange.value = start to end
    }

    private val _sumRange: MutableLiveData<Pair<Float, Float>?> = MutableLiveData()
    val sumRange: LiveData<Pair<Float, Float>?> = _sumRange

    fun setSumRange(from: Float, to: Float) {
        _sumRange.value = from to to
    }

    fun sumRangeToString(): String? {
        val sumRange = this.sumRange.value ?: return null
        val f = sumRange.first.toInt()
        val s = sumRange.second.toInt()
        return "$f..$s"
    }
}