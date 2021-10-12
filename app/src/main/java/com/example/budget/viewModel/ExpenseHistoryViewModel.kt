package com.example.budget.viewModel

import com.example.budget.dto.ExpenseEntity
import com.example.budget.dto.PlannedExpenseEntity
import com.example.budget.repository.api.ExpenseRepository
import com.example.budget.repository.api.PlannedExpenseRepository
import com.example.budget.viewModel.expense.ExpenseViewModel

interface IRecyclerViewModel<T> {
    fun getEntities(groupId:Int, page: Int = 0, callback: (Event<List<T>?>) -> Unit)
}

class ExpenseHistoryViewModel(
    val expenseFiltersViewModel: ExpenseFiltersViewModel,
) : MainViewModel(), IRecyclerViewModel<ExpenseEntity> {
    private val repository = ExpenseRepository

    override fun getEntities(groupId: Int, page: Int, callback: (Event<List<ExpenseEntity>?>) -> Unit) {
        requestWithCallback({
            repository.getExpenses(
                groupId,
                page,
                expenseFiltersViewModel.getFilters())
        }) { callback(it) }
    }
}

class PlannedExpenseHistoryViewModel : MainViewModel(), IRecyclerViewModel<PlannedExpenseEntity> {
    private val repository = PlannedExpenseRepository

    override fun getEntities(groupId: Int, page: Int, callback: (Event<List<PlannedExpenseEntity>?>) -> Unit) {
        requestWithCallback({repository.getPlannedExpenses(groupId)}) { callback(it)}
    }
}