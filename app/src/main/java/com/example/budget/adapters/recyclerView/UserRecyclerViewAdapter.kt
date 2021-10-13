package com.example.budget.adapters.recyclerView

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.budget.databinding.ItemUserBinding
import com.example.budget.dto.UserEntity
import com.example.budget.repository.PersistentRepository.defGroupEntity
import com.example.budget.viewModel.Event
import com.example.budget.viewModel.recyclerView.UserViewModel

class UserRecyclerViewAdapter(private val groupViewModel: UserViewModel) :
    RecyclerView.Adapter<UserRecyclerViewAdapter.UserItemViewHolder>() {

    var list: List<UserEntity> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserItemViewHolder =
        UserItemViewHolder(parent)

    override fun onBindViewHolder(holder: UserItemViewHolder, position: Int) {
        holder.binding.apply {
            userEntity = list[position]
        }
    }

    override fun getItemCount(): Int = list.size

    class UserItemViewHolder(
        parent: ViewGroup,
        val binding: ItemUserBinding =
            ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false),
    ) : RecyclerView.ViewHolder(binding.root)

    init {
        defGroupEntity.observeForever { groupEntity ->
            groupViewModel.getEntities(groupEntity.id, 0, this::onLoadedEvent)
        }
    }

    private fun onLoadedEvent(event: Event<List<UserEntity>?>) {
        when (event) {
            is Event.Success -> {
                list = event.data!!
                notifyDataSetChanged()
            }
            is Event.Error -> Log.d("User", "Error")
            Event.Loading -> Unit
        }
    }

}