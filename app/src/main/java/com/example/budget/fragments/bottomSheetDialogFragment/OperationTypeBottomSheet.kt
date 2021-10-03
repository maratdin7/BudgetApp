package com.example.budget.fragments.bottomSheetDialogFragment

import android.widget.RelativeLayout
import android.widget.TextView
import com.example.budget.R
import com.example.budget.adapters.recyclerView.OperationType
import com.example.budget.repository.view.DialogBuilder
import com.example.budget.viewModel.HeaderItemFilterViewModel

class OperationTypeBottomSheet(viewModel: HeaderItemFilterViewModel, notify: () -> Unit) :
    AbstractRadioGroupBottomSheet(viewModel, notify) {

    override fun DialogBuilder.createFilter(relativeLayout: RelativeLayout, title: TextView) {
        title.text = getString(R.string.operation_type)

        val operationsTypeList = OperationType.values()
        val operationType = viewModel.operationType
        val checkedId: Int = findCheckedId(operationType.getOrNull(), operationsTypeList)

        val radioGroup = radioGroup(operationsTypeList.map { it.type }, checkedId, title)

        radioGroup.settingRadioGroup(relativeLayout) { t -> operationType.setValue(OperationType.findByType(t)) }
    }

    companion object {
        const val TAG = "OperationTypeBottomSheet"
    }
}

