package com.example.budget.viewModel.expense

import com.example.budget.client.NetworkService
import com.example.budget.dto.ExpenseEntity
import com.example.budget.repository.api.ExpenseRepository
import com.example.budget.viewModel.dropDownField.CashAccountViewModel
import com.example.budget.viewModel.dropDownField.CategoryViewModel

open class ExpenseViewModel(
    override val categoryViewModel: CategoryViewModel,
    override val cashAccountViewModel: CashAccountViewModel,
) : AbstractExpenseViewModel<ExpenseEntity>(categoryViewModel, cashAccountViewModel, ExpenseRepository)

