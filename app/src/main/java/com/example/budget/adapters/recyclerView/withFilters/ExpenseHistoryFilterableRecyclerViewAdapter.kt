package com.example.budget.adapters.recyclerView.withFilters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.budget.adapters.recyclerView.AbstractNetRecyclerViewAdapter
import com.example.budget.adapters.recyclerView.DataItem
import com.example.budget.databinding.HeaderExpenseFilterBinding
import com.example.budget.databinding.ItemExpenseHistoryBinding
import com.example.budget.dto.ExpenseEntity
import com.example.budget.dto.IExpenseEntity
import com.example.budget.repository.FormatterRepository
import com.example.budget.repository.PersistentRepository.defGroupEntity
import com.example.budget.viewModel.Event
import com.example.budget.viewModel.ExpenseHistoryViewModel
import com.example.budget.viewModel.IRecyclerViewModel
import com.example.budget.viewModel.wrap.FieldWrapWithError
import java.text.SimpleDateFormat
import java.util.*

typealias PairX<T, K> = androidx.core.util.Pair<T, K>

class ExpenseHistoryFilterableRecyclerViewAdapter(
    val expenseHistoryViewModel: ExpenseHistoryViewModel,
    private val parentFragmentManager: FragmentManager,
) : ExpenseHistoryRecyclerViewAdapter<ExpenseEntity>(expenseHistoryViewModel, FormatterRepository.dayWithFullMonth) {

    private val expenseFiltersViewModel = expenseHistoryViewModel.expenseFiltersViewModel

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        downloadNewPage(position)

        when (holder) {
            is HeaderItemViewHolder -> {
                if (position != 0)
                    throw IllegalArgumentException()

                holder.binding.apply {
                    this.viewModel = expenseFiltersViewModel

                    Filters(parentFragmentManager).apply {
                        expenseFiltersViewModel.let { vm ->
                            createOperationTypeFilter(operationType, vm.operationType)
                            createSortByFilter(sortBy, vm.sortBy)
                            createSumRangeFilter(sumRange, vm.sumRange)
                            createDateRangeFilter(dateRange, vm.dateRange)
                        }
                    }
                    sentFilters.setOnClickListener {
                        clearList()
                        getEntities()
                    }
                }
            }
            is ExpenseItemViewHolder -> {
                expenseItemView(position, holder)
            }
        }
    }

    override fun clearList() {
        super.clearList()
        addEl(DataItem.Header(-1))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            HEADER_VIEW_TYPE_HEADER -> HeaderItemViewHolder(parent)
            ITEM_VIEW_TYPE_HISTORY_ITEM -> ExpenseItemViewHolder(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    class HeaderItemViewHolder(
        parent: ViewGroup,
        val binding: HeaderExpenseFilterBinding =
            HeaderExpenseFilterBinding.inflate(LayoutInflater.from(parent.context), parent, false),
    ) : RecyclerView.ViewHolder(binding.root)

    init {
        val applyObservable = { f: FieldWrapWithError<out Any?, out Any?> ->
            f.data.observeForever { notifyItemChanged(0) }
        }
        with(expenseFiltersViewModel) {
            applyObservable(sortBy)
            applyObservable(operationType)
            applyObservable(dateRange)
            applyObservable(sumRange)
        }
    }

}

open class ExpenseHistoryRecyclerViewAdapter<T : IExpenseEntity>(
    val viewModel: IRecyclerViewModel<T>,
    val dateFormatter: SimpleDateFormat,
) :
    AbstractHistoryRecyclerViewAdapter<T>() {
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

    override fun getEntities() {
        val groupId = defGroupEntity.value?.id ?: return
        viewModel.getEntities(groupId, page) {
            when (it) {
                is Event.Success -> onSuccess(it.data) { i -> ExpenseHistoryItem(i) }
                is Event.Error -> Log.d("expenses", "ERROR")
                Event.Loading -> {
                }
            }
        }
    }

    protected fun expenseItemView(
        position: Int,
        holder: ExpenseItemViewHolder,
    ) {
        val historyItem = getItem(position) as ExpenseHistoryItem
        holder.binding.apply {
            expenseEntity = historyItem.expenseEntity
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
        defGroupEntity.observeForever {
            clearList()
            getEntities()
        }
    }
}

abstract class AbstractHistoryRecyclerViewAdapter<T> : AbstractNetRecyclerViewAdapter<T>() {

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

data class ExpenseHistoryItem(
    val expenseEntity: IExpenseEntity,
) : ItemWithDate(expenseEntity.id + 1, expenseEntity.date)

abstract class ItemWithDate(override val id: Int, val date: Date) : DataItem.Item(id)