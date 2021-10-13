package com.example.budget.client.api

import com.example.budget.dto.UserEntity
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface UserApi {
    @FormUrlEncoded
    @POST("allUsersInGroup")
    suspend fun getAllUsersInGroup(@Field("groupId") groupId: Int): Response<List<UserEntity>>

    @FormUrlEncoded
    @POST("invitationToJoinGroup")
    suspend fun invitationToJoinGroup(
        @Field("groupId") groupId: Int,
        @Field("emailForInvite") emailForInvite: String,
    ): Response<UserEntity>
}