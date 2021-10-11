package com.example.budget.adapters.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.budget.databinding.ItemCategoryBinding

class CategoryRecyclerViewAdapter : AbstractRecyclerViewAdapter() {
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CategoryItemViewHolder -> {
                val categoryItem = getItem(position) as CategoryItem
                holder.bind(categoryItem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HISTORY_ITEM -> CategoryItemViewHolder(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    class CategoryItemViewHolder(
        parent: ViewGroup,
        val binding: ItemCategoryBinding =
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false),
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(categoryItem: CategoryItem) =
            binding.apply {
                categoryField.text = categoryItem.name
                operationType.text = categoryItem.operationType.type
            }
    }
}

data class CategoryItem(
    override val id: Int,
    val name: String = "Category",
    val operationType: OperationType,
) : DataItem.Item(id)

enum class OperationType(val type: String) {
    ALL("Все"),
    EXPENSE("Расходы"),
    INCOME("Доходы");

    companion object {
        fun toList() = listOf(EXPENSE.type, INCOME.type)

        fun toListAll(): List<String> = listOf(ALL.type, EXPENSE.type, INCOME.type)

        fun findByType(type: String): OperationType =
            when (type) {
                EXPENSE.type -> EXPENSE
                INCOME.type -> INCOME
                else -> ALL
            }
    }
}