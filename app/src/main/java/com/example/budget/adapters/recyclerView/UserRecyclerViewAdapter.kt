package com.example.budget.adapters.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.budget.databinding.ItemUserBinding

class UserRecyclerViewAdapter : AbstractRecyclerViewAdapter() {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is UserItemViewHolder -> {
                val userItem = getItem(position) as UserItem
                holder.bind(userItem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HISTORY_ITEM -> UserItemViewHolder(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    class UserItemViewHolder(
        parent: ViewGroup,
        val binding: ItemUserBinding =
            ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false),
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(userItem: UserItem) =
            binding.apply {
                with(userItem) {
                    userEmailField.text = email
                }
            }
    }
}

data class UserItem(
    override val id: Int,
    val email: String = "email@email.com",
) : DataItem.Item(id)