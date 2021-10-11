package com.example.budget.dto

import com.example.budget.repository.FormatterRepository.onlyDayFormatter
import com.google.gson.annotations.SerializedName
import java.util.*

data class PlannedExpenseEntity(
    override var id: Int = 0,
    override val categoryId: Int,
    override val price: Double,
    override val cashAccountId: Int,
    val day: Int,
    override val comment: String?,
    var priority: Int = 0,
    override var refCategoryEntity: CategoryEntity? = null,
    override var refCashAccountEntity: CashAccountEntity? = null,
) : IExpenseEntity {
    override val date: Date
        get() = onlyDayFormatter.parse(day.toString())!!

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