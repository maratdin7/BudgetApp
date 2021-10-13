package com.example.budget.client.api

import com.example.budget.dto.UserEntity
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

interface UserApi {
    @POST("allUsersInGroup")
    suspend fun getAllUsersInGroup(@Query("groupId") groupId: Int): Response<List<UserEntity>>
}