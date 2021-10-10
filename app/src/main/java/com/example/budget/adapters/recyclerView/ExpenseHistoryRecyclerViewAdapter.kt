package com.example.budget.adapters.recyclerView

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
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
import com.example.budget.viewModel.wrap.FieldWrap
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
    private var page = 0
    private var downloadPage = -1
    private val PERCENT_FOR_DOWNLOAD_NEW_PAGE = 0.8

    val headerItemFilterViewModel = expenseHistoryViewModel.headerItemFilterViewModel

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position == downloadPage)
            getExpenses()

        when (holder) {
            is HeaderItemViewHolder -> {
                if (position != 0)
                    throw IllegalArgumentException()

                val header = getItem(position) as DataItem.Header
                holder.bind(header).apply {
                    this.viewModel = headerItemFilterViewModel

                    createOperationTypeFilter(operationType)
                    createSortByFilter(sortBy)
                    createSumRangeFilter(sumRange)
                    createDateRangeFilter(dateRange)

                    sentFilters.setOnClickListener {
                        list.clearList()
                        getExpenses()
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

                            if (prevDate.compareTo(curDate) == 0) {
                                dateFormatter = null
                                return
                            }
                        }
                    }
                    dateFormatter = FormatterRepository.dayWithFullMonth
                }
            }
        }
    }

    private fun getExpenses() {
        val groupId = defGroupEntity.value?.id ?: return
        expenseHistoryViewModel.getExpenses(groupId, page) {
            when (it) {
                is Event.Success -> {
                    downloadPage = if (it.data.isNullOrEmpty()) -1
                    else {
                        page++
                        list.updateList(it.data)
                        Log.d("expenses", "Success")
                        list.size - 1
                    }
                }
                is Event.Error -> Log.d("expenses", "ERROR")
                Event.Loading -> {
                }
            }
        }

    }

    private fun MutableList<DataItem>.updateList(data: List<ExpenseEntity>) {
        val prevSize = this.size
        addAll(data.map { i -> ExpenseHistoryItem(i) })
        this@ExpenseHistoryRecyclerViewAdapter.submitList(this)
        notifyItemRangeInserted(prevSize, data.size - 1)
    }

    private fun MutableList<DataItem>.clearList() {
        page = 0
        clear()
        this.add(DataItem.Header(-1))
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int =
        list.size

    override fun getItemViewType(position: Int): Int =
        when (getItem(position)) {
            is DataItem.Header -> HEADER_VIEW_TYPE_HEADER
            is DataItem.DateItem -> ITEM_VIEW_TYPE_DATE_HEADER
            is DataItem.Item -> ITEM_VIEW_TYPE_HISTORY_ITEM
        }

    private fun createDateRangeFilter(dateRange: Chip) {
        dateRange.setOnClickListener {
            createDatePickerRange()
        }

        dateRange.clearOnCloseIconClickListener(headerItemFilterViewModel.dateRange)
    }

    private fun createDatePickerRange() {
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
        }
    }

    private fun createSumRangeFilter(sumRange: Chip) {
        val sumRangeBottomSheet = SumRangeBottomSheet(headerItemFilterViewModel)
        sumRange.setOnClickListener {
            sumRangeBottomSheet.show(parentFragmentManager, SumRangeBottomSheet.TAG)
        }

        sumRange.clearOnCloseIconClickListener(headerItemFilterViewModel.sumRange)
    }

    private fun createSortByFilter(sortBy: Chip) {
        val sortByBottomSheet = SortByBottomSheet(headerItemFilterViewModel)
        sortBy.setOnClickListener {
            sortByBottomSheet.show(parentFragmentManager, SortByBottomSheet.TAG)
        }

        sortBy.clearOnCloseIconClickListener(headerItemFilterViewModel.sortBy)
    }

    private fun createOperationTypeFilter(operationType: Chip) {
        val operationTypeBottomSheet =
            OperationTypeBottomSheet(headerItemFilterViewModel)
        operationType.setOnClickListener {
            operationTypeBottomSheet.show(parentFragmentManager, OperationTypeBottomSheet.TAG)
        }

        operationType.clearOnCloseIconClickListener(headerItemFilterViewModel.operationType)
    }

    private fun Chip.clearOnCloseIconClickListener(fieldWrap: FieldWrap<out Any?, out Any?>) {
        setOnCloseIconClickListener { fieldWrap.clear() }
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
        defGroupEntity.observeForever {
            list.clearList()
            getExpenses()
        }

        val applyObservable = { f: FieldWrap<out Any?, out Any?> -> f.data.observeForever { notifyItemChanged(0) } }
        with(headerItemFilterViewModel) {
            applyObservable(sortBy)
            applyObservable(operationType)
            applyObservable(dateRange)
            applyObservable(sumRange)
        }
    }

}

class Filters() {

}

data class ExpenseHistoryItem(
    val expenseEntity: ExpenseEntity,
) : DataItem.Item(expenseEntity.id + 1)

