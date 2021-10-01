package com.example.budget.dto

data class ExpensesAns(
    val expenseEntity: ExpenseEntity,
    val categoryEntity: CategoryEntity,
    val cashAccountEntity: CashAccountEntity,
)
