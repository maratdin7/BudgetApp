package com.example.budget.client.api

import com.example.budget.dto.PlannedExpenseEntity
import retrofit2.Response
import retrofit2.http.*

interface PlannedExpenseApi {

    @FormUrlEncoded
    @POST("create")
    suspend fun createPlannedExpense(
        @Field("categoryId") categoryId: Int,
        @Field("cashAccountId") cashAccountId: Int,
        @Field("day") day: Int,
        @Field("price") price: Double,
        @Query("comment") comment: String? = null,
    ): Response<PlannedExpenseEntity>

    @GET("/all")
    suspend fun getAllPlannedExpenses(
        @Query("groupId") groupId: Int,
    ): Response<List<PlannedExpenseEntity>>
}