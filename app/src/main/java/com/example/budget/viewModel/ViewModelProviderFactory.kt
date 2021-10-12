package com.example.budget.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.budget.viewModel.dropDownField.CashAccountViewModel
import com.example.budget.viewModel.dropDownField.CategoryViewModel
import com.example.budget.viewModel.expense.ExpenseViewModel
import com.example.budget.viewModel.expense.IncomeViewModel
import com.example.budget.viewModel.expense.LocalExchangeViewModel
import com.example.budget.viewModel.expense.PlannedExpenseViewModel

object ViewModelProviderFactory : ViewModelProvider.Factory {

    private val categoryViewModel = CategoryViewModel()
    private val cashAccountViewModel = CashAccountViewModel()

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(vm: Class<T>): T {
        return when {
            vm.checkType<T, AuthViewModel>() -> AuthViewModel() as T
            vm.checkType<T, ExpenseViewModel>() -> ExpenseViewModel(categoryViewModel, cashAccountViewModel) as T
            vm.checkType<T, IncomeViewModel>() -> IncomeViewModel(categoryViewModel, cashAccountViewModel) as T
            vm.checkType<T, PlannedExpenseViewModel>() -> PlannedExpenseViewModel(categoryViewModel,
                cashAccountViewModel) as T
            vm.checkType<T, LocalExchangeViewModel>() -> LocalExchangeViewModel(cashAccountViewModel) as T
            vm.checkType<T, ExpenseHistoryViewModel>() -> ExpenseHistoryViewModel(ExpenseFiltersViewModel()) as T
            vm.checkType<T, PlannedExpenseHistoryViewModel>() -> PlannedExpenseHistoryViewModel() as T
            vm.checkType<T, LocalExchangeHistoryViewModel>() -> LocalExchangeHistoryViewModel() as T
            else -> throw IllegalArgumentException("ViewModel Not Found")
        }
    }

    private inline fun <T : ViewModel, reified K : ViewModel> Class<T>.checkType(): Boolean =
        isAssignableFrom(K::class.java)

}