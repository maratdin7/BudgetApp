package com.example.budget.repository.view

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.budget.R
import com.example.budget.viewModel.dropDownField.AbstractDropDownFieldViewModel
import com.google.android.material.textfield.TextInputLayout

class DropDownField<T>(
    private val context: Context,
    private val list: List<T>,
    textInputLayout: TextInputLayout,
) {

    private val autoCompleteTextField: AutoCompleteTextView

    fun setDropDownFieldAdapter(
        entityToString: (entity: T) -> String,
        listItemId: Int = R.layout.item_dropdown,
    ): ArrayAdapter<String> {
        val adapter = ArrayAdapter(context, listItemId, list.map { entityToString(it) })
        autoCompleteTextField.setAdapter(adapter)

        return adapter
    }

    fun setSelection(
        dropDownFieldViewModel: AbstractDropDownFieldViewModel<T>,
        onItemSelected: (Int) -> Unit,
    ) {
        autoCompleteTextField.setOnItemClickListener { _, _, i, _ -> onItemSelected(i) }

        val defaultCashAccount = dropDownFieldViewModel.getDefaultEntity(context)
        autoCompleteTextField.text = null
        val i = list.indexOf(defaultCashAccount)
        if (i != -1)
            autoCompleteTextField.setSelection(i)
    }

    fun clearFieldObserver(): (T?) -> Unit = { liveData: T? ->
        if (liveData == null)
            autoCompleteTextField.text = null
    }


    init {
        val autoComplete = textInputLayout.editText as? AutoCompleteTextView
        autoCompleteTextField = autoComplete ?: throw IllegalArgumentException()
    }
}