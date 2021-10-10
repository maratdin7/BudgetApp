package com.example.budget.fragments.bottomSheetDialogFragment

import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.budget.repository.view.DialogBuilder
import com.example.budget.viewModel.HeaderItemFilterViewModel

abstract class AbstractRadioGroupBottomSheet(viewModel: HeaderItemFilterViewModel) :
    AbstractBottomSheet(viewModel) {

    protected fun DialogBuilder.radioGroup(
        list: List<String>,
        checkedId: Int,
        title: TextView,
    ): RadioGroup = createRadioGroup(requireContext(), list, checkedId).apply {
        layoutParams = defLayoutParams(title)
    }

    protected fun <T> findCheckedId(el: T?, array: Array<T>): Int {
        el?.run {
            val checkedId = array.indexOf(el)
            if (checkedId >= 0) return checkedId
        }
        return 0
    }

    protected fun RadioGroup.settingRadioGroup(relativeLayout: RelativeLayout, setText: (text: String) -> Unit) {
        relativeLayout.addView(this)

        setOnCheckedChangeListener { r, pos ->
            val checked = r.findViewById<RadioButton>(pos)
            setText(checked.text as String)
        }
    }
}