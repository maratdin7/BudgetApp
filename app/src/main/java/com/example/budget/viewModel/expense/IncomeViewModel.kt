package com.example.budget.viewModel.expense

import com.example.budget.viewModel.dropDownField.CashAccountViewModel
import com.example.budget.viewModel.dropDownField.CategoryViewModel

class IncomeViewModel(
    override val categoryViewModel: CategoryViewModel,
    override val cashAccountViewModel: CashAccountViewModel,
) : ExpenseViewModel(categoryViewModel, cashAccountViewModel)