package com.example.budget.dto

data class UserEntity(
    var id: Int = 0,
    var name: String,
    ) {
    override fun toString(): String =
        "Entity of type: ${javaClass.name} ( " +
                "id = $id " +
                "name = $name " +
                ")"
}