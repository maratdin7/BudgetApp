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

class CashAccountRecyclerViewAdapter(private val context: Context) : RecyclerView.Adapter<CashAccountItemViewHolder>() {

    private var lastCheckedPos: Int? = loadDefaultCashAccountId(context)
    private var lastChecked: View? = null

    private var cashAccounts: List<CashAccountItem> = listOf()

    protected val ITEM_VIEW_TYPE_HISTORY_ITEM = 1

    override fun getItemCount(): Int =
        cashAccounts.size

    override fun getItemViewType(position: Int): Int {
        return ITEM_VIEW_TYPE_HISTORY_ITEM
    }

    fun updateList(l: List<CashAccountItem>) {
        cashAccounts = l
    }

    override fun onBindViewHolder(holder: CashAccountItemViewHolder, position: Int) {
        val binding = holder.bind(cashAccounts[position])

        val defaultCashAccount = binding.defaultCashAccount

        binding.card.setOnLongClickListener {
            defaultCashAccount.visibility = VISIBLE
            if (lastCheckedPos != null) {
                lastChecked?.visibility = GONE
            }
            saveDefaultCashAccountId(cashAccounts[position].id)
            lastCheckedPos = position
            lastChecked = defaultCashAccount

            true
        }

        if (lastCheckedPos == position) {
            defaultCashAccount.visibility = VISIBLE
            lastChecked = defaultCashAccount
        }
        else
            defaultCashAccount.visibility = GONE

    }

    private fun saveDefaultCashAccountId(id: Int) {
        val pref = PreferenceManager.getDefaultSharedPreferences(context)
        pref.edit().apply {
            putInt(context.getString(R.string.defaultCashAccount), id)
            apply()
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CashAccountItemViewHolder =
        CashAccountItemViewHolder(parent)
}


class CashAccountItemViewHolder(
    parent: ViewGroup,
    val binding: ItemCashAccountBinding =
        ItemCashAccountBinding.inflate(LayoutInflater.from(parent.context), parent, false),
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(cashAccountItem: CashAccountItem) =
        binding.apply {
            with(cashAccountItem) {
                cashAccountField.text = cashAccountName
                priceField.text = "$cash ₽"
            }
        }
}

data class CashAccountItem(
    override val id: Int,
    val cashAccountName: String = "CashAccount",
    val cash: Double,
) : DataItem.Item(id)