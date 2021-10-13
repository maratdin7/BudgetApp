package com.example.budget.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.budget.viewModel.dropDownField.CashAccountViewModel
import com.example.budget.viewModel.dropDownField.CategoryViewModel
import com.example.budget.viewModel.expense.ExpenseViewModel
import com.example.budget.viewModel.expense.IncomeViewModel
import com.example.budget.viewModel.expense.LocalExchangeViewModel
import com.example.budget.viewModel.expense.PlannedExpenseViewModel
import com.example.budget.viewModel.recyclerView.*

object ViewModelProviderFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(vm: Class<T>): T {
        return when {
            vm.checkType<T, AuthViewModel>() -> AuthViewModel() as T
            vm.checkType<T, ExpenseViewModel>() -> ExpenseViewModel() as T
            vm.checkType<T, IncomeViewModel>() -> IncomeViewModel() as T
            vm.checkType<T, PlannedExpenseViewModel>() -> PlannedExpenseViewModel() as T
            vm.checkType<T, LocalExchangeViewModel>() -> LocalExchangeViewModel() as T
            vm.checkType<T, ExpenseHistoryViewModel>() -> ExpenseHistoryViewModel(FiltersViewModel()) as T
            vm.checkType<T, PlannedExpenseHistoryViewModel>() -> PlannedExpenseHistoryViewModel() as T
            vm.checkType<T, LocalExchangeHistoryViewModel>() -> LocalExchangeHistoryViewModel() as T
            vm.checkType<T, CashAccountViewModel>() -> CashAccountViewModel() as T
            vm.checkType<T, CategoryViewModel>() -> CategoryViewModel() as T
            vm.checkType<T, UserViewModel>() -> UserViewModel() as T
            else -> throw IllegalArgumentException("ViewModel Not Found")
        }
    }

    private inline fun <T : ViewModel, reified K : ViewModel> Class<T>.checkType(): Boolean =
        isAssignableFrom(K::class.java)

}