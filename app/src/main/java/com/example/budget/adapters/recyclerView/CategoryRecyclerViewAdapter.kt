package com.example.budget.adapters.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.budget.databinding.ItemCategoryBinding
import com.example.budget.viewModel.dropDownField.CategoryViewModel

class CategoryRecyclerViewAdapter(private val categoryViewModel: CategoryViewModel) :
    RecyclerView.Adapter<CategoryRecyclerViewAdapter.CategoryItemViewHolder>() {

    override fun getItemCount(): Int = categoryViewModel.getListEntities().value?.size ?: 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CategoryItemViewHolder(parent)

    override fun onBindViewHolder(holder: CategoryItemViewHolder, position: Int) {
        val entities = categoryViewModel.getListEntities().value ?: return
        val binding = holder.binding
        binding.categoryEntity = entities[position]
    }

    class CategoryItemViewHolder(
        parent: ViewGroup,
        val binding: ItemCategoryBinding =
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false),
    ) : RecyclerView.ViewHolder(binding.root)

    init {
        categoryViewModel.getListEntities().observeForever { notifyDataSetChanged() }
    }

}

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