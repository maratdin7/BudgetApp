package com.example.budget.adapters.recyclerView

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.util.Pair
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.budget.R
import com.example.budget.databinding.HeaderExpenseFilterBinding
import com.example.budget.databinding.ItemExpenseHistoryBinding
import com.example.budget.fragments.bottomSheetDialogFragment.OperationTypeBottomSheet
import com.example.budget.fragments.bottomSheetDialogFragment.SortByBottomSheet
import com.example.budget.fragments.bottomSheetDialogFragment.SumRangeBottomSheet
import com.example.budget.viewModel.HeaderItemFilterViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*

class ExpenseHistoryRecyclerViewAdapter(
    private val fragmentActivity: FragmentActivity,
    val context: Context,
    private val parentFragmentManager: FragmentManager,
) :
    AbstractRecyclerViewWithDateAdapter() {

    var formatter: SimpleDateFormat? = null

    private val viewModel: HeaderItemFilterViewModel by lazy {
        ViewModelProvider(fragmentActivity).get(HeaderItemFilterViewModel::class.java)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderItemViewHolder -> {
                val header = getItem(position) as DataItem.Header
                holder.bind(header).apply {
                    this.viewModel = this@ExpenseHistoryRecyclerViewAdapter.viewModel

                    createOperationTypeFilter(position, operationType)
                    createSortByFilter(position, sortBy)
                    createSumRangeFilter(position, sumRange)
                    createDateRangeFilter(position, dateRange)
                }
            }
            is DateItemViewHolder -> {
                val dateHeaderItem = getItem(position) as DataItem.DateItem
                holder.bind(dateHeaderItem)
            }
            is ExpenseItemViewHolder -> {
                val historyItem = getItem(position) as ExpenseHistoryItem
                holder.bind(historyItem)
            }
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

        viewModel.dateRange.value?.let {
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
            viewModel.setDateRange(start, end)
            notifyItemChanged(position)
        }
    }

    private fun createSumRangeFilter(position: Int, sumRange: Chip) {
        val sumRangeBottomSheet = SumRangeBottomSheet(viewModel) { notifyItemChanged(position) }
        sumRange.setOnClickListener {
            sumRangeBottomSheet.show(parentFragmentManager, SumRangeBottomSheet.TAG)
        }
    }

    private fun createSortByFilter(position: Int, sortBy: Chip) {
        val sortByBottomSheet = SortByBottomSheet(viewModel) { notifyItemChanged(position) }
        sortBy.setOnClickListener {
            sortByBottomSheet.show(parentFragmentManager, SortByBottomSheet.TAG)
        }
    }

    private fun createOperationTypeFilter(position: Int, operationType: Chip) {
        val operationTypeBottomSheet = OperationTypeBottomSheet(viewModel) { notifyItemChanged(position) }
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

        fun bind(header: DataItem.Header): HeaderExpenseFilterBinding {
            return binding
        }
    }

    class ExpenseItemViewHolder(
        parent: ViewGroup,
        val binding: ItemExpenseHistoryBinding =
            ItemExpenseHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false),
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(expenseHistoryItem: ExpenseHistoryItem) =
            binding.apply {
                with(expenseHistoryItem) {
                    categoryField.text = category
                    emailField.text = email
                    priceField.text = "$price ₽"
                    commentField.text = comment
                }
            }
    }
}

data class ExpenseHistoryItem(
    override val id: Int,
    val category: String = "Category",
    val email: String = "user@email.com",
    val price: Double = 100.0,
    val comment: String = "hjqwkehjk/nfdfsd/nrewrew/tfdfsd",

    ) : DataItem.Item(id)

