package com.example.budget.fragments.menu

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.budget.R
import com.example.budget.adapters.recyclerView.CashAccountItem
import com.example.budget.adapters.recyclerView.CashAccountRecyclerViewAdapter
import com.example.budget.databinding.FragmentCashAccountBinding
import com.example.budget.repository.view.DialogBuilder

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

        val builder = createDialogBuilder(requireContext(), "Создайте счёт", neutralButtonEnable = false)
        builder.setView(relativeLayout)

        val dialog = builder.createDialog()
        val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        var isNotValidCash = true
        var isNotValidCashAccountName = true
        val isPositiveButtonEnable = { !(isNotValidCashAccountName || isNotValidCash) }

        val checkError = { str: String?, errorStr: String ->
            Boolean
            val isNotValid = str.isNullOrBlank()
            cashAccountNameInputLayout.error = errorStr.takeIf { isNotValid }
            positiveButton.isEnabled = isPositiveButtonEnable()
            isNotValid
        }

        cashAccountNameInputLayout.addTextChangedListener { p0, _, _, _ ->
            isNotValidCashAccountName = checkError(p0, getString(R.string.error_cash_account))
        }

        cashInputLayout.addTextChangedListener { p0, _, _, _ ->
            isNotValidCash = checkError(p0, getString(R.string.error_cash))
        }
    }

    override fun adapterSettings(): RecyclerView.Adapter<RecyclerView.ViewHolder> {
        val adapter = CashAccountRecyclerViewAdapter(requireContext())
        val cashAccounts = tmp()
        adapter.updateList(
            cashAccounts.mapIndexed { i, c ->
                CashAccountItem(id = i,
                    cashAccountName = c.cashAccountName,
                    cash = c.cash)
            })
        return adapter as RecyclerView.Adapter<RecyclerView.ViewHolder>
    }

    private fun tmp(): MutableList<CashAccountEntity> {
        val cashAccounts = mutableListOf<CashAccountEntity>()

        for (i in 0..30) {

            cashAccounts.add(
                CashAccountEntity(cash = i.toDouble())
            )
        }
        return cashAccounts
    }
}

data class CashAccountEntity(
    val cashAccountName: String = "Cash Account",
    val cash: Double,
)

