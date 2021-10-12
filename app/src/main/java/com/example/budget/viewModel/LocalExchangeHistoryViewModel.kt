package com.example.budget.viewModel

import com.example.budget.dto.LocalExchangeEntity
import com.example.budget.repository.api.LocalExchangeRepository

class LocalExchangeHistoryViewModel : MainViewModel(), IRecyclerViewModel<LocalExchangeEntity> {
    private val repository = LocalExchangeRepository

    override fun getEntities(groupId: Int, page: Int, callback: (Event<List<LocalExchangeEntity>?>) -> Unit) {
        requestWithCallback({ repository.getAllLocalExchanges(groupId) }) { callback(it) }
    }

}
