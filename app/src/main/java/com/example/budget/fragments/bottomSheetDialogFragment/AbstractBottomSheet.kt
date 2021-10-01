package com.example.budget.fragments.bottomSheetDialogFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.budget.R
import com.example.budget.databinding.FragmentOperationTypeBinding
import com.example.budget.repository.view.DialogBuilder
import com.example.budget.viewModel.HeaderItemFilterViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class AbstractBottomSheet(val viewModel: HeaderItemFilterViewModel, val notify: () -> Unit) :
    BottomSheetDialogFragment() {

    protected abstract fun DialogBuilder.createFilter(relativeLayout: RelativeLayout, title: TextView)

    protected fun DialogBuilder.defLayoutParams(title: TextView): RelativeLayout.LayoutParams =
        createLayoutParams().apply {
            addRule(RelativeLayout.BELOW, title.id)
            addRule(RelativeLayout.ALIGN_PARENT_START)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentOperationTypeBinding.inflate(layoutInflater, container, false)
        val relativeLayout = binding.relativeLayout
        val title = binding.title
        DialogBuilder.createFilter(relativeLayout, title)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findNavController()
        setStyle(STYLE_NO_FRAME, R.style.CustomBottomSheetDialogTheme)
    }
}