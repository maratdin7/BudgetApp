package com.example.budget.viewModel.recyclerView

import com.example.budget.dto.PlannedExpenseEntity
import com.example.budget.repository.api.PlannedExpenseRepository
import com.example.budget.viewModel.Event
import com.example.budget.viewModel.MainViewModel

class PlannedExpenseHistoryViewModel : MainViewModel(), IRecyclerViewModel<PlannedExpenseEntity> {
    private val repository = PlannedExpenseRepository

    override fun getEntities(groupId: Int, page: Int, callback: (Event<List<PlannedExpenseEntity>?>) -> Unit) {
        requestWithCallback({ repository.getPlannedExpenses(groupId) }) { callback(it)}
    }
}