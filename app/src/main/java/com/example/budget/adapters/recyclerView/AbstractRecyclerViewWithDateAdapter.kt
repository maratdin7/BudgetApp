package com.example.budget.adapters.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.budget.databinding.ItemDateBinding
import com.example.budget.repository.FormatterRepository.dayWithFullMonth
import java.text.SimpleDateFormat
import java.util.*

abstract class AbstractRecyclerViewWithDateAdapter : AbstractRecyclerViewAdapter() {

    fun <T : DateEntity> toDataItem(list: List<T>, headerEnable: Boolean = false): List<DataItem> {
        var prevDate: Date = list.first().date
        val e = mutableListOf<DataItem>()

        var id = 0
        if (headerEnable)
            e.add(DataItem.Header(id++))

        e.add(DataItem.DateItem(id++, prevDate))
        for (expense in list) {
            if (expense.date != prevDate) {
                e.add(expense.toDateItem(id++))
                prevDate = expense.date
            }
            e.add(expense.toItem(id++))
        }
        return e.toList()
    }



    class DateItemViewHolder(
        parent: ViewGroup,
        val binding: ItemDateBinding =
            ItemDateBinding.inflate(LayoutInflater.from(parent.context), parent, false),
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(dateItem: DataItem.DateItem) =
            binding.apply {
                dateText.text = dayWithFullMonth.format(dateItem.date)
            }
    }
}

abstract class DateEntity(open val date: Date) {
    abstract fun toItem(id: Int): DataItem

    open fun toDateItem(id: Int): DataItem = DataItem.DateItem(id, date)
}


