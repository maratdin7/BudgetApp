package com.example.budget.adapters.recyclerView

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.util.Pair
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.budget.R
import com.example.budget.databinding.HeaderExpenseFilterBinding
import com.example.budget.databinding.ItemExpenseHistoryBinding
import com.example.budget.dto.ExpenseEntity
import com.example.budget.fragments.bottomSheetDialogFragment.OperationTypeBottomSheet
import com.example.budget.fragments.bottomSheetDialogFragment.SortByBottomSheet
import com.example.budget.fragments.bottomSheetDialogFragment.SumRangeBottomSheet
import com.example.budget.repository.FormatterRepository
import com.example.budget.repository.PersistentRepository.defGroupEntity
import com.example.budget.viewModel.Event
import com.example.budget.viewModel.ExpenseHistoryViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*

class ExpenseHistoryRecyclerViewAdapter(
    private val expenseHistoryViewModel: ExpenseHistoryViewModel,
    private val parentFragmentManager: FragmentManager,
) : AbstractRecyclerViewWithDateAdapter() {

    var formatter: SimpleDateFormat? = null
    override var list: MutableList<DataItem> = mutableListOf(DataItem.Header(-1))

    val headerItemFilterViewModel = expenseHistoryViewModel.headerItemFilterViewModel

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderItemViewHolder -> {
                if (position != 0)
                    throw IllegalArgumentException()

                val header = getItem(position) as DataItem.Header
                holder.bind(header).apply {
                    this.viewModel = headerItemFilterViewModel

                    createOperationTypeFilter(position, operationType)
                    createSortByFilter(position, sortBy)
                    createSumRangeFilter(position, sumRange)
                    createDateRangeFilter(position, dateRange)

                    sentFilters.setOnClickListener {
                        defGroupEntity.value?.let { groupEntity ->
                            expenseHistoryViewModel.getExpenses(groupEntity.id, 0) {
                                when (it) {
                                    is Event.Success -> {
                                        expenseHistoryViewModel.expenses.setValue(it.data!!.toMutableList())
                                        list.clear()
                                        list.add(DataItem.Header(-1))
                                        list.addAll(it.data.map {i ->  ExpenseHistoryItem(i) })
                                        this@ExpenseHistoryRecyclerViewAdapter.submitList(list)
                                        notifyDataSetChanged()
                                        Log.d("expenses", "Success")
                                    }
                                    is Event.Error -> Log.d("expenses", "ERROR")
                                    Event.Loading -> {}
                                }
                            }
                        }
                    }
                }
            }
            is DateItemViewHolder -> {
                val dateHeaderItem = getItem(position) as DataItem.DateItem
                holder.bind(dateHeaderItem)
            }
            is ExpenseItemViewHolder -> {
                val historyItem = getItem(position) as ExpenseHistoryItem
                holder.bind(historyItem).apply {
                    expenseEntity = historyItem.expenseEntity
                    priceFormatter = FormatterRepository.priceFormatter.apply {
                        maximumFractionDigits = 1
                    }
                    if (list.size > 1) {
                        val prev = list[position - 1]
                        if (prev is ExpenseHistoryItem) {
                            val prevDate = prev.expenseEntity.date
                            val curDate = historyItem.expenseEntity.date

                            if (prevDate.after(curDate))
                                return
                        }
                    }
                    dateFormatter = FormatterRepository.dayWithFullMonth
                }
            }
        }
    }

    override fun getItemCount(): Int =
        list.size

    override fun getItemViewType(position: Int): Int {
        Log.d("expenses", "$position $list\n $itemCount")
        return when (getItem(position)) {
            is DataItem.Header -> HEADER_VIEW_TYPE_HEADER
            is DataItem.DateItem -> ITEM_VIEW_TYPE_DATE_HEADER
            is DataItem.Item -> ITEM_VIEW_TYPE_HISTORY_ITEM
        }
    }

    private fun createDateRangeFilter(position: Int, dateRange: Chip) {
        dateRange.setOnClickListener {
            createDatePickerRange(position)
        }
    }

    private fun createDatePickerRange(position: Int) {
        val now = Calendar.getInstance().timeInMillis
        var curRange = Pair(now, now)

        val dateRange = headerItemFilterViewModel.dateRange
        dateRange.data.value?.let {
            curRange = Pair(it.first.time, it.second.time)
        }

        val datePickerRange = MaterialDatePicker.Builder.dateRangePicker().apply {
            setSelection(curRange)
            setTheme(R.style.ThemeOverlay_MaterialComponents_MaterialCalendar)
        }

        val picker = datePickerRange.build()
        picker.show(parentFragmentManager, picker.toString())

        picker.addOnNegativeButtonClickListener { picker.dismiss() }
        picker.addOnPositiveButtonClickListener {
            val start = Date(it.first!!)
            val end = Date(it.second!!)
            dateRange.setValue(start to end)
            notifyItemChanged(position)
        }
    }

    private fun createSumRangeFilter(position: Int, sumRange: Chip) {
        val sumRangeBottomSheet = SumRangeBottomSheet(headerItemFilterViewModel) { notifyItemChanged(position) }
        sumRange.setOnClickListener {
            sumRangeBottomSheet.show(parentFragmentManager, SumRangeBottomSheet.TAG)
        }
    }

    private fun createSortByFilter(position: Int, sortBy: Chip) {
        val sortByBottomSheet = SortByBottomSheet(headerItemFilterViewModel) { notifyItemChanged(position) }
        sortBy.setOnClickListener {
            sortByBottomSheet.show(parentFragmentManager, SortByBottomSheet.TAG)
        }
    }

    private fun createOperationTypeFilter(position: Int, operationType: Chip) {
        val operationTypeBottomSheet =
            OperationTypeBottomSheet(headerItemFilterViewModel) { notifyItemChanged(position) }
        operationType.setOnClickListener {
            operationTypeBottomSheet.show(parentFragmentManager, OperationTypeBottomSheet.TAG)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            HEADER_VIEW_TYPE_HEADER -> HeaderItemViewHolder(parent)
            ITEM_VIEW_TYPE_DATE_HEADER -> DateItemViewHolder(parent).apply {
                formatter = this@ExpenseHistoryRecyclerViewAdapter.formatter ?: formatter
            }
            ITEM_VIEW_TYPE_HISTORY_ITEM -> ExpenseItemViewHolder(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    class HeaderItemViewHolder(
        parent: ViewGroup,
        val binding: HeaderExpenseFilterBinding =
            HeaderExpenseFilterBinding.inflate(LayoutInflater.from(parent.context), parent, false),
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(header: DataItem.Header): HeaderExpenseFilterBinding = binding
    }

    class ExpenseItemViewHolder(
        parent: ViewGroup,
        val binding: ItemExpenseHistoryBinding =
            ItemExpenseHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false),
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(expenseHistoryItem: ExpenseHistoryItem) = binding
    }

    init {
        expenseHistoryViewModel.expenses.data.observeForever { expenses ->
//            list.addAll(expenses.map { ExpenseHistoryItem(it) })
//            this.submitList(list)
//            notifyDataSetChanged()
        }
    }

}

data class ExpenseHistoryItem(
    val expenseEntity: ExpenseEntity,
) : DataItem.Item(expenseEntity.id + 1)

