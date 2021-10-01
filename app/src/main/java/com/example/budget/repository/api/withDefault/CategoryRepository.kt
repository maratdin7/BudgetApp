package com.example.budget.repository.api.withDefault

import android.content.Context
import com.example.budget.R
import com.example.budget.adapters.recyclerView.OperationType
import com.example.budget.client.api.CategoryApi
import com.example.budget.dto.CategoryEntity
import retrofit2.Response

class CategoryRepository(private val repository: CategoryApi) : DefEntityRepository<CategoryEntity>() {

    override fun Context.getPersistentKey() = getString(R.string.default_category)

    suspend fun createCategory(
        groupId: Int,
        name: String,
        parentId: Int?,
        type: OperationType,
    ): Response<Unit> =
        repository.createCategory(groupId, name, parentId, type)

    suspend fun getAllCategories(
        groupId: Int,
        type: OperationType? = null,
    ): Response<List<CategoryEntity>> =
        repository.getAllCategories(groupId, type)

    suspend fun getCategory(
        categoryId: Int,
    ): Response<CategoryEntity> =
        repository.getCategory(categoryId)

}