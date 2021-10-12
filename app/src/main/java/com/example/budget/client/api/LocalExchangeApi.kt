package com.example.budget.client.api

import com.example.budget.dto.LocalExchangeEntity
import retrofit2.Response
import retrofit2.http.*

interface LocalExchangeApi {

    @FormUrlEncoded
    @POST("create")
    suspend fun createLocalExchange(
        @Field("senderId") senderId: Int,
        @Field("receiverId") receiverId: Int,
        @Field("sent") sent: Double,
        @Field("date") date: String,
        @Query("comment") comment: String? = null,
    ): Response<LocalExchangeEntity>

    @GET("all")
    suspend fun getAllLocalExchanges(
        @Query("groupId") groupId: Int,
    ): Response<List<LocalExchangeEntity>>
}