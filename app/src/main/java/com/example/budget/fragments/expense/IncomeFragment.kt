package com.example.budget.fragments.expense

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.budget.adapters.recyclerView.OperationType
import com.example.budget.databinding.FragmentExpenseBinding
import com.example.budget.dto.CashAccountEntity
import com.example.budget.dto.CategoryEntity
import com.example.budget.dto.ExpenseEntity
import com.example.budget.viewModel.expense.IncomeViewModel
import com.example.budget.viewModel.ViewModelProviderFactory
import java.util.*

class IncomeFragment : AbstractExpenseFragment<ExpenseEntity>() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        super.onCreate(savedInstanceState)
        val binding = FragmentExpenseBinding.inflate(inflater, container, false)
        findNavController()

        val incomeViewModel =
            ViewModelProvider(this, ViewModelProviderFactory).get(IncomeViewModel::class.java)
        incomeViewModel.setFields(binding, OperationType.INCOME)

        return binding.root
    }

    override fun createEntity(
        price: Double,
        date: Date,
        categoryEntity: CategoryEntity,
        cashAccountEntity: CashAccountEntity,
        comment: String?,
    ): ExpenseEntity = ExpenseEntity(
        price = price,
        date = date,
        categoryId = categoryEntity.id,
        cashAccountId = cashAccountEntity.id,
        comment = comment
    )

}