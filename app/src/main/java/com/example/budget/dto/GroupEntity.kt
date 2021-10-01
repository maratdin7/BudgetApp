package com.example.budget.dto

data class GroupEntity(
    val id: Int = 0,
    val name: String,
    val lastChange: String,
    ) {
    override fun toString(): String =
        "Entity of type: ${javaClass.name} ( " +
                "id = $id " +
                "name = $name " +
                "lastChange = $lastChange " +
                ")"
}