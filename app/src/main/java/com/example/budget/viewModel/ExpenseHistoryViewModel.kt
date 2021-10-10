package com.example.budget.viewModel

import com.example.budget.dto.ExpenseEntity
import com.example.budget.repository.PersistentRepository.defGroupEntity
import com.example.budget.repository.api.ExpenseRepository
import com.example.budget.viewModel.wrap.FieldWrap

class ExpenseHistoryViewModel(
    val headerItemFilterViewModel: HeaderItemFilterViewModel,
) : MainViewModel() {
    private val repository = ExpenseRepository

    fun getExpenses(groupId: Int, p: Int = 0, callback: (Event<List<ExpenseEntity>?>) -> Unit) {
        requestWithCallback({
            repository.getExpenses(
                groupId,
                p,
                getFilters())
        }) { callback(it) }
    }

    private fun getFilters(): Filters = headerItemFilterViewModel.run {
        Filters(
            type = operationType.getOrNull(),
            dateRange = dateRange.getOrNull(),
            sumRange = sumRange.getOrNull(),
            direction = sortBy.getOrNull()
        )
    }
}