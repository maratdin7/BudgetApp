package com.example.budget.adapters.recyclerView

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.budget.viewModel.HeaderItemFilterViewModel
import java.util.*

abstract class AbstractRecyclerViewAdapter :
    ListAdapter<DataItem, RecyclerView.ViewHolder>(HistoryDiffCallback()) {

    open var list: MutableList<DataItem> = mutableListOf()

    protected val ITEM_VIEW_TYPE_DATE_HEADER = 0
    protected val ITEM_VIEW_TYPE_HISTORY_ITEM = 1
    protected val HEADER_VIEW_TYPE_HEADER = 2

    abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder

    abstract override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int)

    override fun getItemCount(): Int =
        list.size

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Header -> HEADER_VIEW_TYPE_HEADER
            is DataItem.DateItem -> ITEM_VIEW_TYPE_DATE_HEADER
            is DataItem.Item -> ITEM_VIEW_TYPE_HISTORY_ITEM
        }
    }

    fun updateList(l: List<DataItem>) {
        list = l.toMutableList()
        submitList(list)
    }
}

class HistoryDiffCallback : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }
}

sealed class DataItem {
    abstract val id: Int

    class Header(override val id: Int) : DataItem()
    abstract class Item(override val id: Int) : DataItem()
    data class DateItem(
        override val id: Int,
        val date: Date,
    ) : DataItem()
}
