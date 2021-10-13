package com.example.budget.viewModel.dropDownField

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.budget.dto.GroupEntity
import com.example.budget.repository.api.withDefault.DefEntityRepository
import com.example.budget.repository.PersistentRepository
import com.example.budget.viewModel.Event
import com.example.budget.viewModel.MainViewModel
import com.example.budget.viewModel.recyclerView.IRecyclerViewModel
import com.example.budget.viewModel.wrap.ErrorWrap
import com.example.budget.viewModel.wrap.FieldWrapWithError
import retrofit2.Response

abstract class AbstractDropDownFieldViewModel<T>(
    protected open val repository: DefEntityRepository<T>,
) : MainViewModel(), IRecyclerViewModel<T> {

    protected val listEntities = FieldWrapWithError<List<T>, String>()

    val defEntity = FieldWrapWithError<T?, String>()

    val error = ErrorWrap<String?>()

    fun getDefaultEntity(context: Context): T? {
        defEntity.setValue(loadFromPersistent(context))
        return defEntity.data.value
    }

    fun getEntityFromIndex(index: Int): T? {
        return listEntities.data.value?.run {
            if (index < this.size)
                get(index)
            else null
        }
    }

    protected abstract fun loadFromPersistent(context: Context): T?

    protected abstract fun saveToPersistent(context: Context, entity: T)

    fun setDefaultEntity(context: Context, entity: T) {
        saveToPersistent(context, entity)
        defEntity.setValue(entity)
    }

    fun getListEntities(groupEntity: GroupEntity) {
        getEntities(groupEntity.id, 0, ::onListLoaded)
    }

    private fun onListLoaded(event: Event<List<T>?>) = when (event) {
        is Event.Error -> error.setError(event.message)
        is Event.Success -> {
            listEntities.setValue(event.data!!)
        }
        Event.Loading -> Unit
    }

    final override fun getEntities(groupId: Int, page: Int, callback: (Event<List<T>?>) -> Unit) {
        requestWithCallback({
            loadListEntities(groupId)
        }) { callback(it) }
    }

    fun getListEntities(): LiveData<List<T>> = listEntities.data

    protected abstract suspend fun loadListEntities(groupId: Int): Response<List<T>>

    init {
        PersistentRepository.defGroupEntity.observeForever(::getListEntities)
    }
}