package com.example.budget.fragments.bottomSheetDialogFragment

import android.widget.RelativeLayout
import android.widget.TextView
import com.example.budget.R
import com.example.budget.repository.view.DialogBuilder
import com.example.budget.viewModel.HeaderItemFilterViewModel
import com.google.android.material.slider.RangeSlider

class SumRangeBottomSheet(viewModel: HeaderItemFilterViewModel, notify: () -> Unit) :
    AbstractRadioGroupBottomSheet(viewModel, notify) {

    override fun DialogBuilder.createFilter(relativeLayout: RelativeLayout, title: TextView) {
        val bigValueTo = 500000f
        val valueTo = 10000f

        title.text = getString(R.string.sum_range)

        val sumRange = viewModel.sumRange
        val prevSumRange = sumRange.data.value
        val end =
            if (prevSumRange != null && prevSumRange.second > valueTo)
                bigValueTo
            else valueTo
        val slider = createIntSlider(requireContext(), 0..end.toInt()).apply {
            layoutParams = defLayoutParams(title)
        }

        relativeLayout.addView(slider)

        prevSumRange?.let {
            slider.values = it.toList()
        }

        slider.addOnChangeListener(RangeSlider.OnChangeListener { s, t: Float, _ ->
            val from: Float = s.values[0]
            val to: Float = s.values[1]
            when {
                s.valueTo - t <= 0.5 -> {
                    s.valueTo = bigValueTo
                    s.values[1] = valueTo
                }
            }

            sumRange.setValue(from to to)
            notify()
        })
    }

    companion object {
        const val TAG = "SumRangeBottomSheet"
    }
}

