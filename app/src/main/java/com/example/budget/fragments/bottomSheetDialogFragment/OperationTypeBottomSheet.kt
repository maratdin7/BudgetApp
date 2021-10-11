package com.example.budget.fragments.bottomSheetDialogFragment

import android.widget.RelativeLayout
import android.widget.TextView
import com.example.budget.adapters.recyclerView.OperationType
import com.example.budget.repository.view.DialogBuilder
import com.example.budget.viewModel.wrap.FieldWrapWithError

class OperationTypeBottomSheet(val operationType: FieldWrapWithError<OperationType?, String>) :
    AbstractRadioGroupBottomSheet() {

    override fun DialogBuilder.createFilter(relativeLayout: RelativeLayout, title: TextView) {
        val operationsTypeList = OperationType.values()
        val checkedId: Int = findCheckedId(operationType.getOrNull(), operationsTypeList)

        val radioGroup = radioGroup(operationsTypeList.map { it.type }, checkedId, title)

        radioGroup.settingRadioGroup(relativeLayout) { t -> operationType.setValue(OperationType.findByType(t)) }
    }

    companion object {
        const val TAG = "OperationTypeBottomSheet"
    }
}

