package com.example.budget.viewModel.dropDownField

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.budget.dto.GroupEntity
import com.example.budget.repository.api.withDefault.DefEntityRepository
import com.example.budget.repository.PersistentRepository
import com.example.budget.viewModel.Event
import com.example.budget.viewModel.MainViewModel
import com.example.budget.viewModel.wrap.ErrorWrap
import com.example.budget.viewModel.wrap.FieldWrap
import retrofit2.Response

abstract class AbstractDropDownFieldViewModel<T>(
    protected open val repository: DefEntityRepository<T>,
) : MainViewModel() {

    protected val listEntities = FieldWrap<List<T>, String>()

    val defEntity = FieldWrap<T?, String>()

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

    private fun getListEntities(groupEntity: GroupEntity) {
        requestWithCallback({ getListEntities(groupEntity.id) }) {
            when (it) {
                is Event.Error -> error.setError(it.message)
                is Event.Success -> listEntities.setValue(it.data!!)
                Event.Loading -> Unit
            }
        }
    }

    fun getListEntities(): LiveData<List<T>> = listEntities.data

    protected abstract suspend fun getListEntities(groupId: Int): Response<List<T>>

    init {
        with(PersistentRepository.defGroupEntity) {
            value?.let { getListEntities(it) }
            observeForever { getListEntities(it) }
        }
    }
}