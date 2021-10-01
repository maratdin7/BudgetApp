package com.example.budget.fragments.expense

import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import com.example.budget.R
import com.example.budget.repository.view.DateField
import com.example.budget.repository.view.DialogBuilder
import com.example.budget.repository.view.DropDownField
import com.example.budget.viewModel.Event
import com.example.budget.viewModel.dropDownField.AbstractDropDownFieldViewModel
import com.example.budget.viewModel.expense.AbstractDatePriceViewModel
import com.example.budget.viewModel.wrap.FieldWrap
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.*

abstract class AbstractDatePriceFragment<T, V : AbstractDatePriceViewModel<T>> : Fragment() {

    private fun V.onClickListener(dateFormatter: SimpleDateFormat) {
        getEntityFromFields(dateFormatter)?.let {
            createExpense(it, creationCallback())
        }
    }

    abstract fun V.getEntityFromFields(dateFormatter: SimpleDateFormat): T?

    protected open fun creationCallback(): (Event<T?>) -> Unit = {
        when (it) {
            is Event.Error -> {
                Log.d("Create", "Error ${it.message}")
            }
            is Event.Success -> {
                Log.d("Create", "Success")
            }
            Event.Loading -> {
            }
        }
    }

    private fun TextInputLayout.setOnFocusChangedListener() {
        val onFocusChangeListener =
            View.OnFocusChangeListener { v, h -> DialogBuilder.hideKeyboardOnFocusChanged(requireContext(), v, h) }

        this.editText!!.onFocusChangeListener = onFocusChangeListener
    }

    open fun V.setFields(
        price: TextInputLayout,
        date: TextInputLayout,
        comment: TextInputLayout,
        sentButton: MaterialButton,
        formatter: SimpleDateFormat,
    ) {
        price.setOnFocusChangedListener()
        date.setOnFocusChangedListener()
        comment.setOnFocusChangedListener()

        DateField(formatter).createDatePickerDialog(requireContext(), date) {
            dateField.data.value = it
        }

        sentButton.setOnClickListener { onClickListener(formatter) }
    }

    fun <K> setDropDownField(
        listEntities: LiveData<List<K>>,
        dropDownFieldViewModel: AbstractDropDownFieldViewModel<K>,
        dropDownField: TextInputLayout,
        liveDataWrap: FieldWrap<K?, String>,
        entityToString: (K) -> String,
    ) = listEntities.observe(this@AbstractDatePriceFragment) { list ->
        DropDownField(requireContext(), list, dropDownField).apply {
            setDropDownFieldAdapter(entityToString)
            setSelection(dropDownFieldViewModel) { liveDataWrap.setValue(list[it]) }
            liveDataWrap.data.observe(this@AbstractDatePriceFragment, clearFieldObserver())
        }
    }

    fun V.getDatePrice(formatter: SimpleDateFormat): Pair<Date?, Double?> {
        val price = priceToDouble(getString(R.string.price_field_error))
        val date = dateToDate(formatter, getString(R.string.date_field_error))
        return date to price
    }
}

