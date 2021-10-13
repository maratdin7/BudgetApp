package com.example.budget.adapters.recyclerView

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.budget.adapters.recyclerView.history.AbstractHistoryRecyclerViewAdapter
import com.example.budget.adapters.recyclerView.history.DateEntityItem
import com.example.budget.adapters.recyclerView.history.withFilters.ItemWithDate
import com.example.budget.databinding.ItemLocalExchangeHistoryBinding
import com.example.budget.dto.LocalExchangeEntity
import com.example.budget.repository.FormatterRepository
import com.example.budget.repository.PersistentRepository
import com.example.budget.viewModel.Event
import com.example.budget.viewModel.recyclerView.LocalExchangeHistoryViewModel

class LocalExpenseHistoryRecyclerViewAdapter(localExchangeHistoryViewModel: LocalExchangeHistoryViewModel) :
    AbstractHistoryRecyclerViewAdapter<LocalExchangeEntity>(localExchangeHistoryViewModel) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is LocalExchangeItemViewHolder -> {
                val historyItem = getItem(position) as DateEntityItem<LocalExchangeEntity>
                holder.binding.apply {
                    localExchangeEntity = historyItem.entity
                    priceFormatter = FormatterRepository.priceFormatter(1)

                    dateFormatter =
                        if (isExpenseWithNewDate(position, historyItem))
                            FormatterRepository.dayWithFullMonth
                        else null
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HISTORY_ITEM -> LocalExchangeItemViewHolder(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    class LocalExchangeItemViewHolder(
        parent: ViewGroup,
        val binding: ItemLocalExchangeHistoryBinding =
            ItemLocalExchangeHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false),
    ) : RecyclerView.ViewHolder(binding.root)

    override fun entityToItem(entity: LocalExchangeEntity): DataItem.Item = DateEntityItem(entity)

    override fun onEntitiesLoaded(event: Event<List<LocalExchangeEntity>?>) {
        when (event) {
            is Event.Success -> {
                onSuccess(event.data)
                Log.d("localExchange", "Success")
            }
            is Event.Error -> Log.d("localExchange", "Error")
            Event.Loading -> {}
        }
    }

    init {
        PersistentRepository.defGroupEntity.observeForever {
            clearList()
            getEntities()
        }
    }
}

