package com.example.budget.fragments.menu

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.budget.R
import com.example.budget.adapters.recyclerView.*
import com.example.budget.databinding.FragmentCashAccountBinding
import com.example.budget.repository.PersistentRepository
import com.example.budget.repository.view.DialogBuilder
import com.example.budget.viewModel.Event
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

    private fun getCategoryViewModel() =
        ViewModelProvider(this, ViewModelProviderFactory).get(CategoryViewModel::class.java)

    override fun DialogBuilder.createDialog() {
        val viewModel = getCategoryViewModel()
        val relativeLayout = createRelativeLayout(requireContext())
        var name = ""
        var type = OperationType.EXPENSE

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

        val positiveButtonClickListener = { viewModel.createCategory(name, type) }

        val builder = createDialogBuilder(requireContext(),
            getString(R.string.createCategory), { _, _ -> positiveButtonClickListener()})
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
                            categoryNameInputLayout.error = null
                            name = p0.toString()
                            true
                        }
                }
            })

        categoryTypeRadioGroup.setOnCheckedChangeListener { r, checkedId ->
            val radioButton = r.findViewById<RadioButton>(checkedId)
            type = OperationType.findByType(radioButton.text.toString())
        }
    }

    @SuppressLint("ShowToast")
    private fun CategoryViewModel.createCategory(name: String, type: OperationType) {
        val groupEntity = PersistentRepository.defGroupEntity.value ?: return
        this.createCategoryEntity(groupEntity.id, name, type) {
            when (it) {
                is Event.Success -> getListEntities(groupEntity)
                is Event.Error ->
                    Toast.makeText(requireContext(), "Error when creating the category", Toast.LENGTH_LONG)
                Event.Loading -> Unit
            }
        }
    }

    override fun adapterSettings(): RecyclerView.Adapter<RecyclerView.ViewHolder> {
        val viewModel = ViewModelProvider(this, ViewModelProviderFactory).get(CategoryViewModel::class.java)
        val adapter = CategoryRecyclerViewAdapter(viewModel)
        return adapter as RecyclerView.Adapter<RecyclerView.ViewHolder>
    }
}