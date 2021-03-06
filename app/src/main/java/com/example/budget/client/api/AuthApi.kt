package com.example.budget.client.api

import com.example.budget.adapters.recyclerView.OperationType
import com.example.budget.dto.AuthEntity
import com.example.budget.dto.CashAccountEntity
import com.example.budget.dto.CategoryEntity
import com.example.budget.dto.UserEntity
import retrofit2.Response
import retrofit2.http.*

interface AuthApi {

    @FormUrlEncoded
    @POST("signIn")
    suspend fun signIn(
        @Field("email") email: String,
        @Field("pass") pass: String,
    ): Response<AuthEntity>

    @FormUrlEncoded
    @POST("signUp")
    suspend fun signUp(
        @Field("email") email: String,
        @Field("pass") pass: String,
    ): Response<AuthEntity>

    @FormUrlEncoded
    @POST("generateAccessToken")
    suspend fun generateAccessToken(
        @Field("email") email: String?,
        @Field("refreshToken") refreshToken: String?,
    ): Response<AuthEntity>

    @GET("resetPassword")
    suspend fun resetPassword(@Query("email") email: String): Response<UserEntity>

    @GET("confirmResetPassword")
    suspend fun confirmResetPassword(
        @Query("email") email: String,
        @Query("token") token: Int,
    ): Response<String>
}

interface CategoryApi {

    @FormUrlEncoded
    @POST("create")
    suspend fun createCategory(
        @Field("groupId") groupId: Int,
        @Field("name") name: String,
        @Query("parentId") parentId: Int?,
        @Field("type") type: OperationType,
    ): Response<CategoryEntity>

    @GET("all")
    suspend fun getAllCategories(
        @Query("groupId") groupId: Int,
        @Query("type") type: OperationType?,
    ): Response<List<CategoryEntity>>

    @GET("getCategory")
    suspend fun getCategory(
        @Query("categoryId") categoryId: Int,
    ): Response<CategoryEntity>

}

interface CashAccountApi {

    @FormUrlEncoded
    @POST("create")
    suspend fun createCashAccount(
        @Field("groupId") groupId: Int,
        @Field("name") name: String,
        @Field("cash") cash: Double,
    ): Response<CashAccountEntity>

    @GET("all")
    suspend fun allCashAccounts(
        @Query("groupId") groupId: Int,
    ): Response<List<CashAccountEntity>>

    @GET("getCashAccount")
    suspend fun getCashAccount(
        @Query("groupId") groupId: Int,
        @Query("cashAccountId") cashAccountId: Int,
    ): Response<CashAccountEntity>
}