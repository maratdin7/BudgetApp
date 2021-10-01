package com.example.budget.dto

import com.example.budget.adapters.recyclerView.OperationType

data class CategoryEntity(
    val id: Int,
    val name: String,
    val type: OperationType,
    val parentId: Int?,
    val groupId: Int,
    val refCategoryEntities: List<CategoryEntity>?,
    ) {
    override fun toString(): String =
        "Entity of type: ${javaClass.name} ( " +
                "id = $id " +
                "name = $name " +
                "isIncome = $type " +
                "parentId = $parentId " +
                "groupId = $groupId " +
                ")"
}