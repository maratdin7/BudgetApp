package com.example.budget.viewModel.recyclerView

import com.example.budget.dto.LocalExchangeEntity
import com.example.budget.repository.api.LocalExchangeRepository
import com.example.budget.viewModel.Event
import com.example.budget.viewModel.MainViewModel

class LocalExchangeHistoryViewModel : MainViewModel(), IRecyclerViewModel<LocalExchangeEntity> {
    private val repository = LocalExchangeRepository

    override fun getEntities(groupId: Int, page: Int, callback: (Event<List<LocalExchangeEntity>?>) -> Unit) {
        requestWithCallback({ repository.getAllLocalExchanges(groupId) }) { callback(it) }
    }

}
