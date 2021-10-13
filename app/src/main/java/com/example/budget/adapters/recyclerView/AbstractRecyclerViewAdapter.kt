package com.example.budget.adapters.recyclerView

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.budget.dto.IEntity



abstract class AbstractRecyclerViewAdapter :
    ListAdapter<DataItem, RecyclerView.ViewHolder>(HistoryDiffCallback()) {

    var entities: MutableList<DataItem> = mutableListOf()

    protected val ITEM_VIEW_TYPE_HISTORY_ITEM = 0
    protected val HEADER_VIEW_TYPE_HEADER = 1

    abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder

    abstract override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int)

    override fun getItemCount(): Int =
        entities.size

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Header -> HEADER_VIEW_TYPE_HEADER
            is DataItem.Item -> ITEM_VIEW_TYPE_HISTORY_ITEM
        }
    }

    protected open fun updateList(data: List<DataItem.Item>) {
        with(entities) {
            val prevSize = this.size
            addAll(data)
            notifyItemRangeInserted(prevSize, data.size)
        }
    }

    protected open fun addEl(el: DataItem) {
        with(entities) {
            add(el)
            notifyItemInserted(size - 1)
        }
    }

    protected open fun clearList() {
        entities.clear()
        notifyDataSetChanged()
    }

    fun submitEntities() = submitList(entities)
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
}

open class EntityItem<T : IEntity>(val entity: T) : DataItem.Item(entity.id)
