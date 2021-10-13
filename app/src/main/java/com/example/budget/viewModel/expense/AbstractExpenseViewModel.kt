package com.example.budget.viewModel.expense

import com.example.budget.dto.CashAccountEntity
import com.example.budget.dto.CategoryEntity
import com.example.budget.dto.LocalExchangeEntity
import com.example.budget.repository.PersistentRepository.defGroupEntity
import com.example.budget.repository.api.IExpenseRepository
import com.example.budget.repository.api.LocalExchangeRepository
import com.example.budget.viewModel.Event
import com.example.budget.viewModel.MainViewModel
import com.example.budget.viewModel.wrap.FieldWrapWithError
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

    val commentField = MutableFieldWrap<String, String>()

    open fun clearFields() {
        priceField.clear()
        commentField.clear()
    }

}

abstract class AbstractExpenseViewModel<T>(
    override val repository: IExpenseRepository<T>,
) : AbstractDatePriceViewModel<T>(repository) {

    override fun clearFields() {
        super.clearFields()
        clearDropDownFields()
    }

    var cashAccountField = FieldWrapWithError<CashAccountEntity?, String>()

    var categoryField = FieldWrapWithError<CategoryEntity?, String>()

    private fun clearDropDownFields() {
        cashAccountField.clear()
        categoryField.clear()
    }

    init {
        defGroupEntity.observeForever { clearDropDownFields() }
    }
}

class LocalExchangeViewModel(
    override val repository: LocalExchangeRepository = LocalExchangeRepository,
) : AbstractDatePriceViewModel<LocalExchangeEntity>(repository) {

    var senderField = FieldWrapWithError<CashAccountEntity?, String>()
    var receiverField = FieldWrapWithError<CashAccountEntity?, String>()

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