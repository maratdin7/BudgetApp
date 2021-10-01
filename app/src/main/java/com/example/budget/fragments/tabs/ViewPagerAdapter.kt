package com.example.budget.fragments.tabs

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView.NO_ID
import androidx.viewpager2.adapter.FragmentStateAdapter

class FragmentAdapter(private val fragments: List<Class<out Fragment>>, activity: FragmentActivity) :
    FragmentStateAdapter(activity) {

    var items: List<Int>
        set(value) {
            mutableItems = value.toMutableList()
            notifyDataSetChanged()
        }
        get() = mutableItems

    private var mutableItems = mutableListOf<Int>()

    override fun createFragment(position: Int): Fragment = fragments[position].newInstance()

    override fun getItemId(position: Int): Long {
        if (position < 0) return NO_ID
        return items[position].toLong()
    }

    override fun containsItem(itemId: Long): Boolean {
        return items.contains(itemId.toInt())
    }

    override fun getItemCount(): Int = items.size

    init {
        items = fragments.mapIndexed {i, _ -> i}
    }
}

