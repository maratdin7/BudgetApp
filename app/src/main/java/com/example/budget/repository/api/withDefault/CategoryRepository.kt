package com.example.budget.repository.api.withDefault

import android.content.Context
import com.example.budget.R
import com.example.budget.adapters.recyclerView.OperationType
import com.example.budget.client.NetworkService
import com.example.budget.client.api.CategoryApi
import com.example.budget.dto.CategoryEntity
import retrofit2.Response

object CategoryRepository : DefEntityRepository<CategoryEntity>() {

    private val categoryApi: CategoryApi = NetworkService.create("category/")

    override fun Context.getPersistentKey() = getString(R.string.default_category)

    suspend fun createCategory(
        groupId: Int,
        name: String,
        type: OperationType,
        parentId: Int? = null,
    ): Response<CategoryEntity> =
        categoryApi.createCategory(groupId, name, parentId, type)

    suspend fun getAllCategories(
        groupId: Int,
        type: OperationType? = null,
    ): Response<List<CategoryEntity>> =
        categoryApi.getAllCategories(groupId, type)

    suspend fun getCategory(
        categoryId: Int,
    ): Response<CategoryEntity> =
        categoryApi.getCategory(categoryId)

}