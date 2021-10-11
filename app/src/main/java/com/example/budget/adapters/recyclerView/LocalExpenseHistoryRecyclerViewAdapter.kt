package com.example.budget.adapters.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.budget.databinding.ItemLocalExchangeHistoryBinding

class LocalExpenseHistoryRecyclerViewAdapter  :
    AbstractRecyclerViewAdapter() {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is LocalExchangeItemViewHolder -> {
                val localExchangeHistoryItem = getItem(position) as LocalExchangeHistoryItem
                holder.bind(localExchangeHistoryItem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HISTORY_ITEM -> LocalExchangeItemViewHolder(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    class LocalExchangeItemViewHolder(
        parent:ViewGroup,
        val binding: ItemLocalExchangeHistoryBinding =
            ItemLocalExchangeHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(localExchangeHistoryItem: LocalExchangeHistoryItem) =
            binding.apply {
                with(localExchangeHistoryItem) {
                    senderField.text = senderCashAccount
                    receiverField.text = receiverCashAccount
                    priceField.text = "$price ₽"
                    commentField.text = comment
                }
            }
    }
}

data class LocalExchangeHistoryItem(
    override val id: Int,
    val senderCashAccount: String = "SenderCashAccount",
    val receiverCashAccount: String = "ReceiverCashAccount",
    val price: Double,
    val comment: String = "qqwweee\ndfsfds",
    ) : DataItem.Item(id)