package com.example.budget.dto

import java.util.*

data class ExpenseEntity(
    var id: Int = 0,
    val categoryId: Int,
    val cashAccountId: Int,
    val date: Date,
    val price: Double,
    val comment: String? = null,
    val refCategoryEntity: CategoryEntity? = null,
    val refCashAccountEntity: CashAccountEntity? = null,
    ) {
    override fun toString(): String =
        "Entity of type: ${javaClass.name} ( " +
                "id = $id " +
                "categoryId = $categoryId " +
                "cashAccountId = $cashAccountId " +
                "date = $date " +
                "price = $price " +
                "comment = $comment " +
                ")"
}
