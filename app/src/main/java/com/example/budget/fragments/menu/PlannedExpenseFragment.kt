package com.example.budget.fragments.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.budget.adapters.recyclerView.DataItem
import com.example.budget.adapters.recyclerView.DateEntity
import com.example.budget.adapters.recyclerView.ExpenseHistoryItem
import com.example.budget.adapters.recyclerView.ExpenseHistoryRecyclerViewAdapter
import com.example.budget.databinding.FragmentCashAccountBinding
import com.example.budget.databinding.PanelFullExpenseBinding
import com.example.budget.repository.view.DialogBuilder
import java.text.SimpleDateFormat
import java.util.*


class PlannedExpenseFragment : AbstractMenuFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        super.onCreate(savedInstanceState)
        val binding = FragmentCashAccountBinding.inflate(inflater, container, false)
        findNavController()
        addRecyclerView(binding)

        binding.extendedFab.visibility = View.GONE

        return binding.root
    }

    override fun DialogBuilder.createDialog() {
        val binding = PanelFullExpenseBinding.inflate(layoutInflater)
        val layout = binding.panelFullExpense
        val set = ConstraintSet()
        set.clone(layout)
        layout.layoutParams = createLayoutParamsWithMargin(requireContext())
        layout.setPadding(20.toDp(requireContext()),
            8.toDp(requireContext()),
            20.toDp(requireContext()),
            8.toDp(requireContext()))

        binding.sentExpense.visibility = View.GONE

        val builder = createDialogBuilder(requireContext(),
            "Создание запланированной траты",
            neutralButtonEnable = false)
        builder.setView(layout)
        builder.createDialog()

    }

    override fun adapterSettings(): RecyclerView.Adapter<RecyclerView.ViewHolder> {
        val adapter = ExpenseHistoryRecyclerViewAdapter(requireActivity(), requireContext(), parentFragmentManager)
        adapter.formatter = SimpleDateFormat("d день месяца", Locale.getDefault())
        val expenses = tmp()
        adapter.updateList(adapter.toDataItem(expenses))
        return adapter
    }

    private fun tmp(): MutableList<PlannedExpenseEntity> {
        val expenses = mutableListOf<PlannedExpenseEntity>()
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        var startDate = formatter.parse("2021-12-01")
        for (i in 1..30) {

            when (i) {
                10 -> startDate = formatter.parse("2021-11-22")
                20 -> startDate = formatter.parse("2021-10-21")

            }
            expenses.add(PlannedExpenseEntity(category = "Category $i", price = i.toDouble(), day = i))
        }
        return expenses
    }
}

data class PlannedExpenseEntity(
    val category: String = "Category",
    val email: String = "user@email.com",
    val price: Double = 100.0,
    val day: Int,
    val comment: String = "hjqwkehjk/nfdfsd/nrewrew/tfdfsd",
) : DateEntity(Calendar.getInstance().run {
    set(Calendar.DAY_OF_MONTH, day)
    this.time
}) {

    override fun toItem(id: Int): DataItem = ExpenseHistoryItem(
        id = id,
        category = category,
        price = price,
    )
}