package com.example.budget.client.api

import com.example.budget.adapters.recyclerView.OperationType
import com.example.budget.dto.ExpenseEntity
import com.example.budget.dto.ExpensesAns
import com.example.budget.fragments.bottomSheetDialogFragment.Direction
import retrofit2.Response
import retrofit2.http.*

interface ExpenseApi {

    @FormUrlEncoded
    @POST("create")
    suspend fun createExpense(
        @Field("categoryId") categoryId: Int,
        @Field("cashAccountId") cashAccountId: Int,
        @Field("date") date: String,
        @Field("price") price: Double,
        @Query("comment") comment: String? = null,
    ): Response<ExpenseEntity>

    @GET("getExpenses")
    suspend fun getExpenses(
        @Query("groupId") groupId: Int,
        @Query("page") page: Int,
        @Query("expenseType") operationType: OperationType? = null,
        @Query("categoryId") categoryId: Int? = null,
        @Query("afterDate") afterDate: String? = null,
        @Query("beforeDate") beforeDate: String? = null,
        @Query("from") from: Double? = null,
        @Query("to") to: Double? = null,
        @Query("direction") direction: Direction? = null,
    ): Response<List<ExpenseEntity>>

    @GET("getSum")
    suspend fun getSum(
        @Query("groupId") groupId: Int,
        @Query("expenseType") operationType: OperationType? = null,
        @Query("categoryId") categoryId: Int? = null,
        @Query("afterDate") afterDate: String? = null,
        @Query("beforeDate") beforeDate: String? = null,
        @Query("from") from: Double? = null,
        @Query("to") to: Double? = null,
    ): Response<Double>
}


