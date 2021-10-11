package com.example.budget.fragments.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.budget.R
import com.example.budget.adapters.recyclerView.withFilters.ExpenseHistoryFilterableRecyclerViewAdapter
import com.example.budget.adapters.recyclerView.withFilters.ExpenseHistoryRecyclerViewAdapter
import com.example.budget.databinding.FragmentCashAccountBinding
import com.example.budget.databinding.PanelFullExpenseBinding
import com.example.budget.repository.FormatterRepository
import com.example.budget.repository.view.DialogBuilder
import com.example.budget.viewModel.ExpenseHistoryViewModel
import com.example.budget.viewModel.PlannedExpenseHistoryViewModel
import com.example.budget.viewModel.ViewModelProviderFactory
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
        val viewModel = ViewModelProvider(this, ViewModelProviderFactory).get(PlannedExpenseHistoryViewModel::class.java)
        val formatter = FormatterRepository.onlyDayFormatter(getString(R.string.plannedExpenseDatePost))
        val adapter = ExpenseHistoryRecyclerViewAdapter(viewModel, formatter)
        return adapter
    }
}