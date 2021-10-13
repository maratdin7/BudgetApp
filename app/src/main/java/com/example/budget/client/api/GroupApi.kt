package com.example.budget.client.api

import com.example.budget.dto.GroupEntity
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface GroupApi {

    @FormUrlEncoded
    @POST("create")
    suspend fun createGroup(@Field("groupName") groupName: String): Response<GroupEntity>

    @FormUrlEncoded
    @POST("invitationToJoinGroup")
    suspend fun invitationToJoinGroup(
        @Field("groupId") groupId: Int,
        @Field("emailForInvite") emailForInvite: String,
    ): Response<Unit>

    @GET("allMyGroups")
    suspend fun getAllUserGroups(): Response<List<GroupEntity>>
}