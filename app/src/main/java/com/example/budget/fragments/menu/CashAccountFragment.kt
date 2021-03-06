package com.example.budget.fragments.menu

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.budget.R
import com.example.budget.adapters.recyclerView.CashAccountRecyclerViewAdapter
import com.example.budget.databinding.FragmentCashAccountBinding
import com.example.budget.repository.PersistentRepository.defGroupEntity
import com.example.budget.repository.view.DialogBuilder
import com.example.budget.viewModel.Event
import com.example.budget.viewModel.ViewModelProviderFactory
import com.example.budget.viewModel.dropDownField.CashAccountViewModel
import com.google.android.material.textfield.TextInputLayout

class CashAccountFragment : AbstractMenuFragment() {

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
        val viewModel = getCashAccountViewModel()
        var isNotValidCash = true
        var isNotValidCashAccountName = true
        val isPositiveButtonEnable = { !(isNotValidCashAccountName || isNotValidCash) }
        var name = ""
        var cash = 0.0

        val relativeLayout = createRelativeLayout(requireContext())

        val cashAccountNameInputLayout =
            createTextInputLayoutWithEditText(requireContext(), "Название счёта").apply {
                layoutParams = createLayoutParamsWithMargin(requireContext()).apply {
                    addRule(RelativeLayout.ALIGN_PARENT_TOP)
                    addRule(RelativeLayout.ALIGN_PARENT_START)
                }
            }

        val cashInputLayout = createTextInputLayoutWithEditText(requireContext(), "Количество средств").apply {
            layoutParams = createLayoutParamsWithMargin(requireContext()).apply {
                addRule(RelativeLayout.BELOW, cashAccountNameInputLayout.id)
                addRule(RelativeLayout.ALIGN_PARENT_START)
            }
            editText?.setRawInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
        }

        relativeLayout.addView(cashAccountNameInputLayout)
        relativeLayout.addView(cashInputLayout)

        val positiveButtonClickListener = { viewModel.createCashAccount(name, cash) }

        val builder =
            createDialogBuilder(requireContext(), "Создайте счёт", { _, _ -> positiveButtonClickListener() })
        builder.setView(relativeLayout)

        val dialog = builder.createDialog()
        val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)

        val checkError = { inputLayout: TextInputLayout, str: String?, errorStr: String ->
            Boolean
            val isNotValid = str.isNullOrBlank()
            inputLayout.error = errorStr.takeIf { isNotValid }
            positiveButton.isEnabled = isPositiveButtonEnable()
            isNotValid
        }

        cashAccountNameInputLayout.addTextChangedListener { p0, _, _, _ ->
            isNotValidCashAccountName =
                checkError(cashAccountNameInputLayout, p0, getString(R.string.error_cash_account))
            name = p0.toString()
        }

        cashInputLayout.addTextChangedListener { p0, _, _, _ ->
            var p = p0
            val err = runCatching { p0?.toDouble() ?: throw NullPointerException() }
            cash = err.getOrElse {
                p = null
                0.0
            }
            isNotValidCash = checkError(cashInputLayout, p, getString(R.string.error_cash))
        }

    }

    @SuppressLint("ShowToast")
    private fun CashAccountViewModel.createCashAccount(name: String, cash: Double) {
        val groupEntity = defGroupEntity.value ?: return
        this.createCashAccountEntity(groupEntity.id, name, cash) {
            when (it) {
                is Event.Success -> getListEntities(groupEntity)
                is Event.Error ->
                    Toast.makeText(requireContext(), "Error when creating the cash account", Toast.LENGTH_LONG)
                Event.Loading -> Unit
            }
        }
    }

    private fun getCashAccountViewModel() =
        ViewModelProvider(this, ViewModelProviderFactory).get(CashAccountViewModel::class.java)

    override fun adapterSettings(): RecyclerView.Adapter<RecyclerView.ViewHolder> {
        val adapter = CashAccountRecyclerViewAdapter(requireContext(), getCashAccountViewModel())
        return adapter as RecyclerView.Adapter<RecyclerView.ViewHolder>
    }
}

