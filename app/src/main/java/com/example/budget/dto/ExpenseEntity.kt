package com.example.budget.dto

import com.example.budget.repository.FormatterRepository
import java.util.*

interface IExpenseEntity {
    var id: Int
    val categoryId: Int
    val cashAccountId: Int
    val date: Date
    val price: Double
    val comment: String?
    val refCategoryEntity: CategoryEntity?
    val refCashAccountEntity: CashAccountEntity?
}

class ExpenseEntity(
    override var id: Int = 0,
    override val categoryId: Int,
    override val cashAccountId: Int,
    override var date: Date,
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