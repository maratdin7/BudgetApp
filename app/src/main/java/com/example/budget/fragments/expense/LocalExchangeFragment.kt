package com.example.budget.fragments.expense

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.budget.R
import com.example.budget.databinding.FragmentLocalExchangeBinding
import com.example.budget.dto.LocalExchangeEntity
import com.example.budget.repository.FormatterRepository
import com.example.budget.viewModel.ViewModelProviderFactory
import com.example.budget.viewModel.dropDownField.CashAccountViewModel
import com.example.budget.viewModel.expense.LocalExchangeViewModel
import java.text.SimpleDateFormat

class LocalExchangeFragment : AbstractDatePriceFragment<LocalExchangeEntity, LocalExchangeViewModel>() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        super.onCreate(savedInstanceState)
        val binding = FragmentLocalExchangeBinding.inflate(inflater, container, false)
        findNavController()

        val localExchangeViewModel =
            ViewModelProvider(this, ViewModelProviderFactory).get(LocalExchangeViewModel::class.java)

        val cashAccountViewModel =
            ViewModelProvider(this, ViewModelProviderFactory).get(CashAccountViewModel::class.java)

        binding.localExchangeViewModel = localExchangeViewModel
        binding.lifecycleOwner = this

        with(localExchangeViewModel) {
            val senderCashAccountField = binding.senderCashAccountField.dropdownMenu
            cashAccountViewModel.also { vm ->
                setDropDownField(
                    vm.getListEntities(),
                    vm,
                    senderCashAccountField,
                    senderField
                ) { it.name }

                val receiverCashAccountField = binding.receiverCashAccountField.dropdownMenu
                setDropDownField(
                    vm.getListEntities(),
                    vm,
                    receiverCashAccountField,
                    receiverField
                ) { it.name }
            }
        }

        with(binding.datePrice) {
            localExchangeViewModel.setFields(
                priceField,
                dateField,
                binding.commentField,
                binding.sentExpense,
                FormatterRepository.fullDateFormatter
            )
        }

        return binding.root
    }

    override fun LocalExchangeViewModel.getEntityFromFields(dateFormatter: SimpleDateFormat): LocalExchangeEntity? {
        val (date, price) = getDatePrice(dateFormatter)

        val senderCashAccount = senderField.getOrNull(getString(R.string.necessary_field_error))

        val receiverCashAccount = receiverField.getOrNull(getString(R.string.necessary_field_error))

        val comment = commentField.data.value

        return if (price == null || date == null || senderCashAccount == null || receiverCashAccount == null) {
            Log.d("Create", "Error")
            null
        } else
            LocalExchangeEntity(
                senderId = senderCashAccount.id,
                receiverId = receiverCashAccount.id,
                sent = price,
                date = date,
                comment = comment
            )
    }

}
