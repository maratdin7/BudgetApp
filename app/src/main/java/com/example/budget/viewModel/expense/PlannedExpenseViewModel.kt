package com.example.budget.viewModel.expense

import com.example.budget.client.NetworkService
import com.example.budget.dto.PlannedExpenseEntity
import com.example.budget.repository.api.PlannedExpenseRepository
import com.example.budget.viewModel.dropDownField.CashAccountViewModel
import com.example.budget.viewModel.dropDownField.CategoryViewModel

class PlannedExpenseViewModel(
    override val categoryViewModel: CategoryViewModel,
    override val cashAccountViewModel: CashAccountViewModel,
    override val repository: PlannedExpenseRepository = PlannedExpenseRepository(NetworkService.create("plannedExpense/")),
) : AbstractExpenseViewModel<PlannedExpenseEntity>(categoryViewModel, cashAccountViewModel, repository)