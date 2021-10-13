package com.example.budget.viewModel.dropDownField

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.budget.adapters.recyclerView.OperationType
import com.example.budget.client.NetworkService
import com.example.budget.dto.CashAccountEntity
import com.example.budget.dto.CategoryEntity
import com.example.budget.repository.api.withDefault.CategoryRepository
import com.example.budget.viewModel.Event
import com.example.budget.viewModel.wrap.FieldWrapWithError
import retrofit2.Response

class CategoryViewModel(
    override val repository: CategoryRepository = CategoryRepository,
) : AbstractDropDownFieldViewModel<CategoryEntity>(repository) {

    fun createCategoryEntity(
        groupId: Int,
        name: String,
        operationType: OperationType,
        callback: (Event<CategoryEntity?>) -> Unit,
    ) {
        requestWithCallback({ repository.createCategory(groupId, name, operationType) }) { callback(it) }
    }

    override fun loadFromPersistent(context: Context): CategoryEntity? =
        repository.loadFromPersistent<CategoryEntity>(context)

    override fun saveToPersistent(context: Context, entity: CategoryEntity) =
        repository.saveToPersistent(context, entity)

    override suspend fun loadListEntities(groupId: Int): Response<List<CategoryEntity>> =
        repository.getAllCategories(groupId)

    private val incomeCategories = FieldWrapWithError<List<CategoryEntity>, String>()
    private val expenseCategories = FieldWrapWithError<List<CategoryEntity>, String>()

    fun getListEntities(type: OperationType): LiveData<List<CategoryEntity>> = when (type) {
        OperationType.ALL -> listEntities.data
        OperationType.EXPENSE -> expenseCategories.data
        OperationType.INCOME -> incomeCategories.data
    }

    private fun operationTypeFilter(listCategories: List<CategoryEntity>, type: OperationType) =
        listCategories.filter { it.type == type }

    private fun setCategories(listCategories: List<CategoryEntity>) {
        incomeCategories.setValue(operationTypeFilter(listCategories, OperationType.INCOME))
        expenseCategories.setValue(operationTypeFilter(listCategories, OperationType.EXPENSE))
    }

    init {
        listEntities.data.observeForever { setCategories(it) }
    }

}