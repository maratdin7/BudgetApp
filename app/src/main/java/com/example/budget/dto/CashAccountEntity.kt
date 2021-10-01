package com.example.budget.dto

data class CashAccountEntity(
    val id: Int,
    val accessId: Int,
    val name: String,
    var cash: Double,
    ) {
    override fun toString(): String =
        "Entity of type: ${javaClass.name} ( " +
                "id = $id " +
                "accessId = $accessId " +
                "name = $name " +
                "cash = $cash " +
                ")"
}

