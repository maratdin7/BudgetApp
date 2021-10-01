package com.example.budget.repository.api

import com.example.budget.client.api.PlannedExpenseApi
import com.example.budget.dto.PlannedExpenseEntity
import retrofit2.Response

class PlannedExpenseRepository(private val plannedExpenseApi: PlannedExpenseApi) :
    IExpenseRepository<PlannedExpenseEntity> {

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

}