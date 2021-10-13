package com.example.budget.viewModel.expense

import com.example.budget.dto.ExpenseEntity
import com.example.budget.repository.api.ExpenseRepository

open class ExpenseViewModel : AbstractExpenseViewModel<ExpenseEntity>(ExpenseRepository)

