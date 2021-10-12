package com.example.budget.adapters.recyclerView

import com.example.budget.repository.PersistentRepository
import com.example.budget.viewModel.Event
import com.example.budget.viewModel.IRecyclerViewModel

abstract class AbstractNetRecyclerViewAdapter<T>(open val viewModel: IRecyclerViewModel<T>) :
    AbstractRecyclerViewAdapter() {

    protected var page = 0
    protected var downloadNextPageOnId = -1

    abstract fun entityToItem(entity: T): DataItem.Item

    abstract fun onEntitiesLoaded(event: Event<List<T>?>)

    fun downloadNewPage(position: Int) {
        if (position == downloadNextPageOnId)
            getEntities()
    }

    protected fun getEntities() {
        val groupId = PersistentRepository.defGroupEntity.value?.id ?: return
        viewModel.getEntities(groupId, page, ::onEntitiesLoaded)
    }

    override fun clearList() {
        super.clearList()
        page = 0
    }

    override fun updateList(data: List<DataItem.Item>) {
        super.updateList(data)
        page++
    }

    protected open fun onSuccess(data: List<T>?) {
        downloadNextPageOnId = if (data.isNullOrEmpty()) -1
        else {
            updateList(data.map(::entityToItem))
            entities.size - 1
        }
    }
}