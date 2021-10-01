package com.example.budget.fragments.history

import androidx.recyclerview.widget.RecyclerView
import com.example.budget.adapters.recyclerView.DataItem
import com.example.budget.adapters.recyclerView.DateEntity
import com.example.budget.adapters.recyclerView.ExpenseHistoryRecyclerViewAdapter
import com.example.budget.adapters.recyclerView.ExpenseHistoryItem
import java.text.SimpleDateFormat
import java.util.*

class ExpenseHistoryFragment : AbstractHistoryFragment() {

    override fun adapterSettings(): RecyclerView.Adapter<RecyclerView.ViewHolder> {
        val adapter = ExpenseHistoryRecyclerViewAdapter(requireActivity(), requireContext(), parentFragmentManager)
        val expenses = tmp()
        adapter.updateList(adapter.toDataItem(expenses, true))
        return adapter
    }

    private fun tmp(): MutableList<Expense> {
        val expenses = mutableListOf<Expense>()
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        var startDate = formatter.parse("2021-12-01")
        for (i in 0..30) {

            when (i) {
                10 -> startDate = formatter.parse("2021-11-22")
                20 -> startDate = formatter.parse("2021-10-21")

            }
            expenses.add(Expense(category = "Category $i", price = i.toDouble(), date = startDate))
        }
        return expenses
    }
}

data class Expense(
    val category: String = "Category",
    val email: String = "user@email.com",
    val price: Double = 100.0,
    override val date: Date,
    val comment: String = "hjqwkehjk/nfdfsd/nrewrew/tfdfsd",
) : DateEntity(date) {

    override fun toItem(id: Int): DataItem = ExpenseHistoryItem(
        id = id,
        category = category,
        price = price,
    )
}