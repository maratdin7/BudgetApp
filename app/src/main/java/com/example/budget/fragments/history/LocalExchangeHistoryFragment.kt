package com.example.budget.fragments.history

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.budget.adapters.recyclerView.LocalExpenseHistoryRecyclerViewAdapter
import com.example.budget.viewModel.LocalExchangeHistoryViewModel
import com.example.budget.viewModel.ViewModelProviderFactory

class LocalExchangeHistoryFragment : AbstractHistoryFragment() {

    override fun adapterSettings(): RecyclerView.Adapter<RecyclerView.ViewHolder> {
        val viewModel =
            ViewModelProvider(this, ViewModelProviderFactory).get(LocalExchangeHistoryViewModel::class.java)
        val adapter = LocalExpenseHistoryRecyclerViewAdapter(viewModel).apply {
            submitEntities()
        }
        return adapter
    }
}
