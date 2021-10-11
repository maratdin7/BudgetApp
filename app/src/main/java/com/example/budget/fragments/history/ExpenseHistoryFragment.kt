package com.example.budget.fragments.history

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.budget.adapters.recyclerView.withFilters.ExpenseHistoryFilterableRecyclerViewAdapter
import com.example.budget.adapters.recyclerView.withFilters.ExpenseHistoryRecyclerViewAdapter
import com.example.budget.viewModel.ExpenseHistoryViewModel
import com.example.budget.viewModel.ViewModelProviderFactory

class ExpenseHistoryFragment : AbstractHistoryFragment() {

    override fun adapterSettings(): RecyclerView.Adapter<RecyclerView.ViewHolder> {
        val viewModel = ViewModelProvider(this, ViewModelProviderFactory).get(ExpenseHistoryViewModel::class.java)
        val adapter = ExpenseHistoryFilterableRecyclerViewAdapter(viewModel, parentFragmentManager).apply {
            submitEntities()
        }
        return adapter
    }
}