package com.example.budget.fragments.bottomSheetDialogFragment

import android.widget.RadioGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.budget.R
import com.example.budget.repository.view.DialogBuilder
import com.example.budget.viewModel.HeaderItemFilterViewModel

class SortByBottomSheet(viewModel: HeaderItemFilterViewModel, notify: () -> Unit) :
    AbstractRadioGroupBottomSheet(viewModel, notify) {

    override fun DialogBuilder.createFilter(relativeLayout: RelativeLayout, title: TextView) {
        title.text = getString(R.string.sort_by)

        val dirTypes = Direction.values()
        val sortBy = viewModel.sortBy
        val checkedId: Int = findCheckedId(sortBy.data.value, dirTypes)

        val radioGroup: RadioGroup = radioGroup(dirTypes.map { it.dir }, checkedId, title)

        radioGroup.settingRadioGroup(relativeLayout) { t -> sortBy.setValue(Direction.findByDir(t)) }
    }

    companion object {
        const val TAG = "SortByBottomSheet"
    }
}

enum class Direction(val dir: String) {
    BY_DATE("По умолчанию"),
    ASC("По возрастанию"),
    DESC("По убыванию");

    companion object {
        fun toList() = listOf(BY_DATE.dir, ASC.dir, DESC.dir)

        fun findByDir(dir: String): Direction {
            return when (dir) {
                ASC.dir -> ASC
                DESC.dir -> DESC
                else -> BY_DATE
            }
        }
    }

}