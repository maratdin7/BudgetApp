package com.example.budget.fragments.expense

import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.budget.R
import com.example.budget.adapters.recyclerView.OperationType
import com.example.budget.databinding.FragmentExpenseBinding
import com.example.budget.dto.CashAccountEntity
import com.example.budget.dto.CategoryEntity
import com.example.budget.repository.FormatterRepository.fullDateFormatter
import com.example.budget.repository.view.DropDownField
import com.example.budget.viewModel.MainViewModel
import com.example.budget.viewModel.ViewModelProviderFactory
import com.example.budget.viewModel.dropDownField.CashAccountViewModel
import com.example.budget.viewModel.dropDownField.CategoryViewModel
import com.example.budget.viewModel.expense.AbstractExpenseViewModel
import kotlinx.coroutines.cancel
import java.text.SimpleDateFormat
import java.util.*

abstract class AbstractExpenseFragment<T> :
    AbstractDatePriceFragment<T, AbstractExpenseViewModel<T>>() {

    abstract fun createEntity(
        price: Double,
        date: Date,
        categoryEntity: CategoryEntity,
        cashAccountEntity: CashAccountEntity,
        comment: String?,
    ): T

    override fun AbstractExpenseViewModel<T>.getEntityFromFields(dateFormatter: SimpleDateFormat): T? {
        val (date, price) = getDatePrice(dateFormatter)

        val categoryEntity = categoryField.getOrNull(getString(R.string.necessary_field_error))

        val cashAccountEntity = cashAccountField.getOrNull(getString(R.string.necessary_field_error))

        val comment = commentField.data.value

        return if (price == null || date == null || categoryEntity == null || cashAccountEntity == null) {
            Log.d("Create", "Error")
            null
        } else
            createEntity(price, date, categoryEntity, cashAccountEntity, comment)
    }

    fun AbstractExpenseViewModel<T>.setFields(
        binding: FragmentExpenseBinding,
        type: OperationType = OperationType.ALL,
        formatter: SimpleDateFormat = fullDateFormatter,
    ) {
        val cashAccountViewModel = ViewModelProvider(this@AbstractExpenseFragment,
            ViewModelProviderFactory).get(CashAccountViewModel::class.java)

        val categoryViewModel = ViewModelProvider(this@AbstractExpenseFragment,
            ViewModelProviderFactory).get(CategoryViewModel::class.java)

        binding.expenseViewModel = this
        binding.lifecycleOwner = this@AbstractExpenseFragment

        val expBind = binding.expense


        val category = expBind.categoryField.dropdownMenu
        val cashAccount = expBind.cashAccountField.dropdownMenu

        setDropDownField(
            categoryViewModel.getListEntities(type),
            categoryViewModel,
            category,
            categoryField
        ) { it.name }

        setDropDownField(
            cashAccountViewModel.getListEntities(),
            cashAccountViewModel,
            cashAccount,
            cashAccountField
        ) { it.name }


        with(expBind.datePrice) {
            setFields(
                priceField,
                dateField,
                expBind.commentField,
                expBind.sentExpense,
                formatter
            )
        }
    }
}