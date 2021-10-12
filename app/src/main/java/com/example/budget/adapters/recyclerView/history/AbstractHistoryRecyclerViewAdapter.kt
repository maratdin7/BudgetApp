package com.example.budget.adapters.recyclerView.history

import com.example.budget.adapters.recyclerView.AbstractNetRecyclerViewAdapter
import com.example.budget.adapters.recyclerView.history.withFilters.ItemWithDate
import com.example.budget.dto.IDateEntity
import com.example.budget.viewModel.IRecyclerViewModel

abstract class AbstractHistoryRecyclerViewAdapter<T : IDateEntity>(viewModel: IRecyclerViewModel<T>) :
    AbstractNetRecyclerViewAdapter<T>(viewModel) {

    protected fun isExpenseWithNewDate(
        position: Int,
        historyItem: ItemWithDate,
    ): Boolean {
        if (position >= 1) {
            val prev = entities[position - 1]
            if (prev is ItemWithDate) {
                val prevDate = prev.date
                val curDate = historyItem.date

                return prevDate.compareTo(curDate) != 0
            }
        }
        return true
    }
}