package com.example.budget.viewModel.recyclerView

import com.example.budget.dto.ExpenseEntity
import com.example.budget.repository.api.ExpenseRepository
import com.example.budget.viewModel.Event
import com.example.budget.viewModel.MainViewModel

interface IRecyclerViewModel<T> {
    fun getEntities(groupId:Int, page: Int = 0, callback: (Event<List<T>?>) -> Unit)
}

class ExpenseHistoryViewModel(
    val filtersViewModel: FiltersViewModel,
) : MainViewModel(), IRecyclerViewModel<ExpenseEntity> {
    private val repository = ExpenseRepository

    override fun getEntities(groupId: Int, page: Int, callback: (Event<List<ExpenseEntity>?>) -> Unit) {
        requestWithCallback({
            repository.getExpenses(
                groupId,
                page,
                filtersViewModel.getFilters())
        }) { callback(it) }
    }
}

