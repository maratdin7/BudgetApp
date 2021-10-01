package com.example.budget.viewModel.expense

import com.example.budget.client.NetworkService
import com.example.budget.dto.CashAccountEntity
import com.example.budget.dto.CategoryEntity
import com.example.budget.dto.LocalExchangeEntity
import com.example.budget.repository.api.IExpenseRepository
import com.example.budget.repository.api.LocalExchangeRepository
import com.example.budget.repository.PersistentRepository.defGroupEntity
import com.example.budget.viewModel.dropDownField.CashAccountViewModel
import com.example.budget.viewModel.dropDownField.CategoryViewModel
import com.example.budget.viewModel.Event
import com.example.budget.viewModel.MainViewModel
import com.example.budget.viewModel.wrap.FieldWrap
import com.example.budget.viewModel.wrap.MutableFieldWrap
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

abstract class AbstractDatePriceViewModel<T>(
    protected open val repository: IExpenseRepository<T>,
) : MainViewModel() {

    fun createExpense(entity: T, callback: (Event<T?>) -> Unit) {
        requestWithCallback({ repository.create(entity) }) {
            callback(it)
            clearFields()
        }
    }

    val priceField = MutableFieldWrap<String, String>()

    fun priceToDouble(errorMsg: String): Double? {
        return try {
            val priceStr = priceField.data.value ?: "null"
            if (priceStr.isEmpty())
                throw NumberFormatException()

            priceStr.run {
                if (first() == '.') replaceBefore('.', "0.")
                if (last() == '.') replaceAfter('.', ".0")
                toDouble()
            }
        } catch (e: NumberFormatException) {
            priceField.setError(errorMsg)
            null
        }
    }

    val dateField = MutableFieldWrap<String, String>()

    fun dateToDate(formatter: SimpleDateFormat, errorMsg: String): Date? {
        try {
            return dateField.data.value?.let { formatter.parse(it) } ?: throw ParseException("", 0)
        } catch (e: ParseException) {
            dateField.setError(errorMsg)
        }
        return null
    }

    val commentField = FieldWrap<String, String>()

    open fun clearFields() {
        priceField.clear()
        commentField.clear()
    }

}

abstract class AbstractExpenseViewModel<T>(
    open val categoryViewModel: CategoryViewModel,
    open val cashAccountViewModel: CashAccountViewModel,
    override val repository: IExpenseRepository<T>,
) : AbstractDatePriceViewModel<T>(repository) {

    override fun clearFields() {
        super.clearFields()
        clearDropDownFields()
    }

    var cashAccountField = FieldWrap<CashAccountEntity?, String>()

    var categoryField = FieldWrap<CategoryEntity?, String>()

    private fun clearDropDownFields() {
        cashAccountField.clear()
        categoryField.clear()
    }

    init {
        defGroupEntity.observeForever { clearDropDownFields() }
    }
}

class LocalExchangeViewModel(
    val cashAccountViewModel: CashAccountViewModel,
    override val repository: LocalExchangeRepository = LocalExchangeRepository(NetworkService.create("localExchange/")),
) : AbstractDatePriceViewModel<LocalExchangeEntity>(repository) {

    var senderField = FieldWrap<CashAccountEntity?, String>()
    var receiverField = FieldWrap<CashAccountEntity?, String>()

    override fun clearFields() {
        super.clearFields()
        clearDropDownFields()
    }

    private fun clearDropDownFields() {
        senderField.clear()
        receiverField.clear()
    }

    init {
        defGroupEntity.observeForever { clearDropDownFields() }
    }

}