package com.example.budget.dto

data class PlannedExpenseEntity(
    val id: Int = 0,
    val categoryId: Int,
    val price: Double,
    val cashAccountId: Int,
    val day: Int,
    val comment: String?,
    val priority: Int = 0,
    val refCategoryEntity: CategoryEntity? = null,
    val refCashAccountEntity: CashAccountEntity? = null,
    ) {
    override fun toString(): String =
        "Entity of type: ${javaClass.name} ( " +
                "id = $id " +
                "categoryId = $categoryId " +
                "price = $price " +
                "cashAccountId = $cashAccountId " +
                "day = $day " +
                "comment = $comment " +
                "priority = $priority " +
                ")"

}