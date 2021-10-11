package com.example.budget.fragments.history

import androidx.recyclerview.widget.RecyclerView
import com.example.budget.adapters.recyclerView.DataItem
import com.example.budget.adapters.recyclerView.LocalExchangeHistoryItem
import com.example.budget.adapters.recyclerView.LocalExpenseHistoryRecyclerViewAdapter
import java.text.SimpleDateFormat
import java.util.*

class LocalExchangeHistoryFragment : AbstractHistoryFragment() {

    override fun adapterSettings(): RecyclerView.Adapter<RecyclerView.ViewHolder> {
        val adapter = LocalExpenseHistoryRecyclerViewAdapter()
        val localExchanges = tmp()
//        adapter.updateList(adapter.toDataItem(localExchanges))
        return adapter
    }

    private fun tmp(): MutableList<LocalExchangeEntity> {
        val localExchangeEntity = mutableListOf<LocalExchangeEntity>()
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        var startDate = formatter.parse("2021-12-01")
        for (i in 0..30) {

            when (i) {
                10 -> startDate = formatter.parse("2021-11-22")
                20 -> startDate = formatter.parse("2021-10-21")

            }
            localExchangeEntity.add(
                LocalExchangeEntity(
                    price = i.toDouble(),
                    date = startDate)
            )
        }
        return localExchangeEntity
    }
}

data class LocalExchangeEntity(
    val senderCashAccount: String = "SenderCashAccount",
    val receiverCashAccount: String = "ReceiverCashAccount",
    val price: Double,
    val date: Date,
    val comment: String = "qqwweee\ndfsfds",
) : DataItem.Item(1) {

    fun toItem(id: Int): DataItem = LocalExchangeHistoryItem(
        id = id,
        senderCashAccount = senderCashAccount,
        receiverCashAccount = receiverCashAccount,
        price = price
    )

}
