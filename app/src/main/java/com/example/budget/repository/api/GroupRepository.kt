package com.example.budget.repository.api

import android.content.Context
import com.example.budget.R
import com.example.budget.client.ResponseWrapper
import com.example.budget.client.api.GroupApi
import com.example.budget.dto.GroupEntity
import com.example.budget.dto.UserEntity
import com.example.budget.repository.api.withDefault.DefEntityRepository
import retrofit2.Response

class GroupRepository(private val groupApi: GroupApi): DefEntityRepository<GroupEntity>() {

    private var groupEntity: GroupEntity? = null

    override fun Context.getPersistentKey():String = getString(R.string.default_group)

    suspend fun createGroup(groupName: String) = groupApi.createGroup(groupName)

    suspend fun invitationToJoinGroup(groupId: Int, emailForInvite: String) {
        groupApi.invitationToJoinGroup(groupId, emailForInvite)
    }

    suspend fun getAllUserGroups(): Response<List<GroupEntity>> = groupApi.getAllUserGroups()

    suspend fun getAllUsersInGroup(groupId: Int): ResponseWrapper<List<UserEntity>> =
        groupApi.getAllUsersInGroup(groupId)
}