package com.example.budget.repository.api

import com.example.budget.client.NetworkService
import com.example.budget.client.api.PlannedExpenseApi
import com.example.budget.dto.PlannedExpenseEntity
import retrofit2.Response

object PlannedExpenseRepository :
    IExpenseRepository<PlannedExpenseEntity> {

    private val plannedExpenseApi: PlannedExpenseApi = NetworkService.create("plannedExpense/")

    override suspend fun create(
        entity: PlannedExpenseEntity,
    ): Response<PlannedExpenseEntity> = entity.run {
        return plannedExpenseApi.createPlannedExpense(
            categoryId,
            cashAccountId,
            day,
            price,
            comment
        )
    }

    suspend fun getPlannedExpenses(groupId: Int): Response<List<PlannedExpenseEntity>> =
        plannedExpenseApi.getAllPlannedExpenses(groupId)

}