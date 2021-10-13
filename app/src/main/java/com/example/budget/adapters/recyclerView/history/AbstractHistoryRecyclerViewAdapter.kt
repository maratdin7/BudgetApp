package com.example.budget.adapters.recyclerView.history

import com.example.budget.adapters.recyclerView.AbstractNetRecyclerViewAdapter
import com.example.budget.adapters.recyclerView.DataItem
import com.example.budget.adapters.recyclerView.history.withFilters.ItemWithDate
import com.example.budget.dto.IDateEntity
import com.example.budget.viewModel.recyclerView.IRecyclerViewModel

abstract class AbstractHistoryRecyclerViewAdapter<T : IDateEntity>(viewModel: IRecyclerViewModel<T>) :
    AbstractNetRecyclerViewAdapter<T>(viewModel) {

    override fun entityToItem(entity: T): DataItem.Item = DateEntityItem(entity)

    protected fun isExpenseWithNewDate(
        position: Int,
        historyItem: DateEntityItem<T>,
    ): Boolean {
        if (position >= 1) {
            val prev = entities[position - 1]
            if (prev is DateEntityItem<*>) {
                val prevDate = prev.entity.date
                val curDate = historyItem.entity.date

                return prevDate.compareTo(curDate) != 0
            }
        }
        return true
    }
}