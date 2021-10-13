package com.example.budget.dto

import java.util.*

interface IExpenseEntity : IDateEntity {
    override var id: Int
    val categoryId: Int
    val cashAccountId: Int
    override val date: Date
    val price: Double
    val comment: String?
    val refCategoryEntity: CategoryEntity?
    val refCashAccountEntity: CashAccountEntity?
}

interface IDateEntity : IEntity {
    val date: Date
}

interface IEntity {val id: Int}

class ExpenseEntity(
    override var id: Int = 0,
    override val categoryId: Int,
    override val cashAccountId: Int,
    override val date: Date,
    override val price: Double,
    override val comment: String? = null,
    override val refCategoryEntity: CategoryEntity? = null,
    override val refCashAccountEntity: CashAccountEntity? = null,
) : IExpenseEntity {
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