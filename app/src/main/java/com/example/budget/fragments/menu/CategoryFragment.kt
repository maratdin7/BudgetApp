package com.example.budget.fragments.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.budget.R
import com.example.budget.adapters.recyclerView.*
import com.example.budget.databinding.FragmentCashAccountBinding
import com.example.budget.repository.view.DialogBuilder
import com.example.budget.viewModel.ViewModelProviderFactory
import com.example.budget.viewModel.dropDownField.CashAccountViewModel
import com.example.budget.viewModel.dropDownField.CategoryViewModel

class CategoryFragment : AbstractMenuFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        super.onCreate(savedInstanceState)
        val binding = FragmentCashAccountBinding.inflate(inflater, container, false)
        findNavController()
        addRecyclerView(binding)

        binding.extendedFab.setOnClickListener {
            DialogBuilder.createDialog()
        }

        return binding.root
    }

    override fun DialogBuilder.createDialog() {
        val relativeLayout = createRelativeLayout(requireContext())

        val categoryNameInputLayout =
            createTextInputLayoutWithEditText(requireContext(),
                getString(R.string.request_category_name)).apply {
                layoutParams = createLayoutParamsWithMargin(requireContext()).apply {
                    addRule(RelativeLayout.ALIGN_PARENT_START)
                    addRule(RelativeLayout.ALIGN_PARENT_TOP)
                }
            }

        val categoryTypeRadioGroup = createRadioGroup(requireContext(), OperationType.toList()).apply {
            layoutParams = createLayoutParamsWithMargin(requireContext()).apply {
                addRule(RelativeLayout.BELOW, categoryNameInputLayout.id)
                addRule(RelativeLayout.ALIGN_PARENT_START)
            }
        }

        relativeLayout.addView(categoryNameInputLayout)
        relativeLayout.addView(categoryTypeRadioGroup)

        val builder = createDialogBuilder(requireContext(),
            getString(R.string.createCategory))
        builder.setView(relativeLayout)

        val dialog = builder.createDialog()

        categoryNameInputLayout.editText?.addTextChangedListener(
            object : DialogBuilder.AbstractTextWatcher() {
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled =
                        if (p0.isNullOrBlank()) {
                            categoryNameInputLayout.error = categoryNameInputLayout.hint
                            false
                        } else {
                            categoryNameInputLayout.error = ""
                            true
                        }
                }
            })
    }

    override fun adapterSettings(): RecyclerView.Adapter<RecyclerView.ViewHolder> {
        val viewModel = ViewModelProvider(this, ViewModelProviderFactory).get(CategoryViewModel::class.java)
        val adapter = CategoryRecyclerViewAdapter(viewModel)
        return adapter as RecyclerView.Adapter<RecyclerView.ViewHolder>
    }
}