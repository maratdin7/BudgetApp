package com.example.budget.client.api

import com.example.budget.client.ResponseWrapper
import com.example.budget.dto.GroupEntity
import com.example.budget.dto.UserEntity
import retrofit2.Response
import retrofit2.http.*

interface GroupApi {

    @FormUrlEncoded
    @POST("create")
    suspend fun createGroup(@Field("groupName") groupName: String): Response<GroupEntity>

    @FormUrlEncoded
    @POST("invitationToJoinGroup")
    suspend fun invitationToJoinGroup(@Field("groupId") groupId: Int, @Field("emailForInvite") emailForInvite: String): ResponseWrapper<Unit>

    @GET("allMyGroups")
    suspend fun getAllUserGroups(): Response<List<GroupEntity>>

    @FormUrlEncoded
    @POST("allUsersInGroup")
    suspend fun getAllUsersInGroup(@Query("groupId") groupId: Int): ResponseWrapper<List<UserEntity>>

}