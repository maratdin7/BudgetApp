package com.example.budget.repository.api

import com.example.budget.client.NetworkService
import com.example.budget.client.api.ExpenseApi
import com.example.budget.dto.ExpenseEntity
import com.example.budget.repository.FormatterRepository.sqlDateFormatter
import com.example.budget.viewModel.recyclerView.Filters
import retrofit2.Response
import java.util.*

interface IExpenseRepository<T> {
    suspend fun create(entity: T): Response<T>
}

object ExpenseRepository : IExpenseRepository<ExpenseEntity> {

    private val expenseApi: ExpenseApi = NetworkService.create("expense/")

    override suspend fun create(
        entity: ExpenseEntity,
    ): Response<ExpenseEntity> = entity.run {
        return expenseApi.createExpense(
            categoryId = categoryId,
            cashAccountId = cashAccountId,
            date = sqlDateFormatter.format(date),
            price = price,
            comment = comment
        )
    }

    suspend fun getExpenses(groupId: Int, page: Int, filters: Filters): Response<List<ExpenseEntity>> {
        val dateFormatter = { date: Date? -> date?.let { sqlDateFormatter.format(it) } }

        filters.run {
            return expenseApi.getExpenses(
                groupId = groupId,
                page = page,
                operationType = type,
                categoryId = categoryId,
                afterDate = dateFormatter(dateRange?.first),
                beforeDate = dateFormatter(dateRange?.second),
                from = sumRange?.first?.toDouble(),
                to = sumRange?.second?.toDouble(),
                direction = direction
            )
        }
    }

}