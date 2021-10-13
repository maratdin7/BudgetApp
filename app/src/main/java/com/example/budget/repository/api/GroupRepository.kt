package com.example.budget.repository.api

import android.content.Context
import com.example.budget.R
import com.example.budget.client.NetworkService
import com.example.budget.client.api.GroupApi
import com.example.budget.client.api.UserApi
import com.example.budget.dto.GroupEntity
import com.example.budget.dto.UserEntity
import com.example.budget.repository.api.withDefault.DefEntityRepository
import retrofit2.Response

object UserRepository {
    private val userApi: UserApi = NetworkService.create("group/")

    suspend fun getAllUsersInGroup(groupId: Int): Response<List<UserEntity>> =
        userApi.getAllUsersInGroup(groupId)

    suspend fun invitationToJoinGroup(groupId: Int, emailForInvite: String) =
        userApi.invitationToJoinGroup(groupId, emailForInvite)
}

object GroupRepository : DefEntityRepository<GroupEntity>() {

    private val groupApi: GroupApi = NetworkService.create("group/")

    private var groupEntity: GroupEntity? = null

    override fun Context.getPersistentKey(): String = getString(R.string.default_group)

    suspend fun createGroup(groupName: String) = groupApi.createGroup(groupName)

    suspend fun getAllUserGroups(): Response<List<GroupEntity>> = groupApi.getAllUserGroups()
}