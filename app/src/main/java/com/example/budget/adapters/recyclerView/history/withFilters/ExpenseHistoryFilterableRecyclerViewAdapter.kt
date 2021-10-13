package com.example.budget.adapters.recyclerView.history.withFilters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.budget.adapters.recyclerView.DataItem
import com.example.budget.adapters.recyclerView.EntityItem
import com.example.budget.adapters.recyclerView.history.ExpenseHistoryRecyclerViewAdapter
import com.example.budget.databinding.HeaderExpenseFilterBinding
import com.example.budget.dto.ExpenseEntity
import com.example.budget.dto.IExpenseEntity
import com.example.budget.repository.FormatterRepository
import com.example.budget.viewModel.recyclerView.ExpenseHistoryViewModel
import com.example.budget.viewModel.wrap.FieldWrapWithError
import java.util.*

typealias PairX<T, K> = androidx.core.util.Pair<T, K>

class ExpenseHistoryFilterableRecyclerViewAdapter(
    expenseHistoryViewModel: ExpenseHistoryViewModel,
    private val parentFragmentManager: FragmentManager,
) : ExpenseHistoryRecyclerViewAdapter<ExpenseEntity>(expenseHistoryViewModel, FormatterRepository.dayWithFullMonth) {

    private val expenseFiltersViewModel = expenseHistoryViewModel.filtersViewModel

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

data class ExpenseHistoryItem(
    val expenseEntity: IExpenseEntity,
) : ItemWithDate(expenseEntity.id + 1, expenseEntity.date)



abstract class ItemWithDate(override val id: Int, val date: Date) : DataItem.Item(id)