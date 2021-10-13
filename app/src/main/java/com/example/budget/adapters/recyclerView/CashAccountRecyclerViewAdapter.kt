package com.example.budget.adapters.recyclerView

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.example.budget.repository.PersistentRepository.loadDefaultCashAccountId
import com.example.budget.R
import com.example.budget.databinding.ItemCashAccountBinding
import com.example.budget.repository.FormatterRepository
import com.example.budget.viewModel.dropDownField.CashAccountViewModel

class CashAccountRecyclerViewAdapter(
    private val context: Context,
    private val cashAccountViewModel: CashAccountViewModel,
) : RecyclerView.Adapter<CashAccountRecyclerViewAdapter.CashAccountItemViewHolder>() {

    private var lastCheckedPos: Int? = loadDefaultCashAccountId(context)
    private var lastChecked: View? = null

    private fun saveDefaultCashAccountId(id: Int) {
        val pref = PreferenceManager.getDefaultSharedPreferences(context)
        pref.edit().apply {
            putInt(context.getString(R.string.defaultCashAccount), id)
            apply()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CashAccountItemViewHolder =
        CashAccountItemViewHolder(parent)


    override fun onBindViewHolder(holder: CashAccountItemViewHolder, position: Int) {
        val entities = cashAccountViewModel.getListEntities().value ?: return
        val binding = holder.binding
        binding.cashAccountEntity = entities[position]
        binding.priceFormatter = FormatterRepository.priceFormatter(1)
        val defaultCashAccount = binding.defaultCashAccount

        binding.card.setOnLongClickListener {
            defaultCashAccount.visibility = VISIBLE
            if (lastCheckedPos != null) {
                lastChecked?.visibility = GONE
            }
            saveDefaultCashAccountId(entities[position].id)
            lastCheckedPos = position
            lastChecked = defaultCashAccount

            true
        }

        if (lastCheckedPos == position) {
            defaultCashAccount.visibility = VISIBLE
            lastChecked = defaultCashAccount
        } else
            defaultCashAccount.visibility = GONE
    }

    override fun getItemCount(): Int = cashAccountViewModel.getListEntities().value?.size ?: 0

    class CashAccountItemViewHolder(
        parent: ViewGroup,
        val binding: ItemCashAccountBinding =
            ItemCashAccountBinding.inflate(LayoutInflater.from(parent.context), parent, false),
    ) : RecyclerView.ViewHolder(binding.root)


    init {
        cashAccountViewModel.getListEntities().observeForever { notifyDataSetChanged() }
    }

}