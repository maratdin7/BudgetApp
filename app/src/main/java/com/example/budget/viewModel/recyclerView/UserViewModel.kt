package com.example.budget.viewModel.recyclerView

import com.example.budget.dto.UserEntity
import com.example.budget.repository.api.UserRepository
import com.example.budget.viewModel.Event
import com.example.budget.viewModel.MainViewModel
import com.example.budget.viewModel.recyclerView.IRecyclerViewModel

class UserViewModel : MainViewModel(), IRecyclerViewModel<UserEntity> {
    private val repository = UserRepository

    override fun getEntities(groupId: Int, page: Int, callback: (Event<List<UserEntity>?>) -> Unit) {
        requestWithCallback({ repository.getAllUsersInGroup(groupId) }) { callback(it) }
    }
}