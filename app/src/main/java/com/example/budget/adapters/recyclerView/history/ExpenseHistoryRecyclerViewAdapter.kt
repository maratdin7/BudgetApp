package com.example.budget.adapters.recyclerView.history

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.budget.adapters.recyclerView.DataItem
import com.example.budget.adapters.recyclerView.EntityItem
import com.example.budget.adapters.recyclerView.history.withFilters.ExpenseHistoryItem
import com.example.budget.databinding.ItemExpenseHistoryBinding
import com.example.budget.dto.IDateEntity
import com.example.budget.dto.IExpenseEntity
import com.example.budget.repository.FormatterRepository
import com.example.budget.repository.PersistentRepository
import com.example.budget.viewModel.Event
import com.example.budget.viewModel.recyclerView.IRecyclerViewModel
import java.text.SimpleDateFormat

open class ExpenseHistoryRecyclerViewAdapter<T : IExpenseEntity>(
    override val viewModel: IRecyclerViewModel<T>,
    val dateFormatter: SimpleDateFormat,
) : AbstractHistoryRecyclerViewAdapter<T>(viewModel) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HISTORY_ITEM -> ExpenseItemViewHolder(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ExpenseItemViewHolder -> expenseItemView(position, holder)
        }
    }

    override fun onEntitiesLoaded(event: Event<List<T>?>) {
        when (event) {
            is Event.Success -> {
                onSuccess(event.data)
                Log.d("expenses", "Success")
            }
            is Event.Error -> Log.d("expenses", "ERROR")
            Event.Loading -> {
            }
        }
    }

    protected fun expenseItemView(
        position: Int,
        holder: ExpenseItemViewHolder,
    ) {
        val historyItem = getItem(position) as DateEntityItem<T>
        holder.binding.apply {
            expenseEntity = historyItem.entity
            priceFormatter = FormatterRepository.priceFormatter(1)

            dateFormatter =
                if (isExpenseWithNewDate(position, historyItem))
                    this@ExpenseHistoryRecyclerViewAdapter.dateFormatter
                else null
        }
    }

    class ExpenseItemViewHolder(
        parent: ViewGroup,
        val binding: ItemExpenseHistoryBinding =
            ItemExpenseHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false),
    ) : RecyclerView.ViewHolder(binding.root)

    init {
        PersistentRepository.defGroupEntity.observeForever {
            clearList()
            getEntities()
        }
    }
}

class DateEntityItem<T : IDateEntity>(entity: T) : EntityItem<T>(entity)