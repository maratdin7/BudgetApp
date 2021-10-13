package com.example.budget.dto

data class CashAccountEntity(
    override var id: Int = 0,
    val accessId: Int,
    val name: String,
    var cash: Double,
    ) : IEntity {
    override fun toString(): String =
        "Entity of type: ${javaClass.name} ( " +
                "id = $id " +
                "accessId = $accessId " +
                "name = $name " +
                "cash = $cash " +
                ")"
}

