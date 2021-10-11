package com.example.budget.fragments.expense

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.budget.R
import com.example.budget.databinding.FragmentExpenseBinding
import com.example.budget.dto.CashAccountEntity
import com.example.budget.dto.CategoryEntity
import com.example.budget.dto.PlannedExpenseEntity
import com.example.budget.repository.FormatterRepository.onlyDayFormatter
import com.example.budget.viewModel.expense.PlannedExpenseViewModel
import com.example.budget.viewModel.ViewModelProviderFactory
import java.util.*

class PlannedExpenseFragment : AbstractExpenseFragment<PlannedExpenseEntity>() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        super.onCreate(savedInstanceState)
        val binding = FragmentExpenseBinding.inflate(inflater, container, false)
        findNavController()

        val plannedExpenseViewModel =
            ViewModelProvider(this, ViewModelProviderFactory).get(PlannedExpenseViewModel::class.java)

        plannedExpenseViewModel.setFields(binding, formatter = onlyDayFormatter)

        binding.expense.datePrice.dateField.hint = getString(R.string.day_of_expense)

        return binding.root
    }

    override fun createEntity(
        price: Double,
        date: Date,
        categoryEntity: CategoryEntity,
        cashAccountEntity: CashAccountEntity,
        comment: String?,
    ): PlannedExpenseEntity {
        return PlannedExpenseEntity(
            price = price,
            day = onlyDayFormatter.format(date).toInt(),
            categoryId = categoryEntity.id,
            cashAccountId = cashAccountEntity.id,
            comment = comment
        )
    }

}
