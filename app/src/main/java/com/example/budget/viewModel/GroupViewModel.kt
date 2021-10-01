package com.example.budget.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.budget.client.NetworkService
import com.example.budget.dto.GroupEntity
import com.example.budget.repository.api.GroupRepository

class GroupViewModel : MainViewModel() {

    private val groupRepository: GroupRepository = GroupRepository(NetworkService.create("group/"))

    private val _curGroupEntity: MutableLiveData<GroupEntity> = MutableLiveData()
    val curGroupEntity: LiveData<GroupEntity> = _curGroupEntity

    private val _groupsList: MutableLiveData<List<GroupEntity>> = MutableLiveData()
    val groupsList: LiveData<List<GroupEntity>> = _groupsList

    fun setGroupsList(groupsList: List<GroupEntity>) {
        _groupsList.value = groupsList
    }

    fun setCurGroupEntity(groupEntity: GroupEntity?) {
        _curGroupEntity.value = groupEntity
    }

    fun getAllUserGroups(callback: (event: Event<List<GroupEntity>?>) -> Unit) {
        requestWithCallback({ groupRepository.getAllUserGroups() }) { callback(it) }
    }

    fun loadGroupEntity(context: Context) {
        _curGroupEntity.value = groupRepository.loadFromPersistent<GroupEntity>(context)
    }

    fun saveGroupEntity(context: Context, groupEntity: GroupEntity) {
        groupRepository.saveToPersistent(context, groupEntity)
        _curGroupEntity.value = groupEntity
    }

    fun createGroup(groupName: String, callback: (event: Event<GroupEntity?>) -> Unit) {
        requestWithCallback({ groupRepository.createGroup(groupName) }) { callback(it) }
    }
}