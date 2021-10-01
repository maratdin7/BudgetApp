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

        val operationsTypeList = OperationType.toListAll()
        val checkedId: Int = viewModel.operationType.value.let {
            if (it != null)
                operationsTypeList.indexOf(it)
            else 0
        }

        val radioGroup = radioGroup(operationsTypeList, checkedId, title)

        radioGroup.settingRadioGroup(relativeLayout) { t -> viewModel.setOperationType(t) }
    }

    companion object {
        const val TAG = "OperationTypeBottomSheet"
    }
}

