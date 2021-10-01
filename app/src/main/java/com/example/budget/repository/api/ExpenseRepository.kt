package com.example.budget.repository.api

import com.example.budget.client.api.ExpenseApi
import com.example.budget.client.api.LocalExchangeApi
import com.example.budget.dto.ExpenseEntity
import com.example.budget.dto.LocalExchangeEntity
import com.example.budget.repository.FormatterRepository.sqlDateFormatter
import retrofit2.Response

interface IExpenseRepository<T> {
    suspend fun create(entity: T): Response<T>
}

class ExpenseRepository(private val expenseApi: ExpenseApi) : IExpenseRepository<ExpenseEntity> {

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

}

class LocalExchangeRepository(private val localExchangeApi: LocalExchangeApi): IExpenseRepository<LocalExchangeEntity> {

    override suspend fun create(
        entity: LocalExchangeEntity,
    ): Response<LocalExchangeEntity> = entity.run {
        return localExchangeApi.createLocalExchange(
            senderId = senderId,
            receiverId = receiverId,
            sent = sent,
            date = sqlDateFormatter.format(date)
        )
    }
}