package com.example.budget.repository.api

import com.example.budget.client.api.LocalExchangeApi
import com.example.budget.dto.LocalExchangeEntity
import com.example.budget.repository.FormatterRepository
import retrofit2.Response

class LocalExchangeRepository(private val localExchangeApi: LocalExchangeApi): IExpenseRepository<LocalExchangeEntity> {

    override suspend fun create(
        entity: LocalExchangeEntity,
    ): Response<LocalExchangeEntity> = entity.run {
        return localExchangeApi.createLocalExchange(
            senderId = senderId,
            receiverId = receiverId,
            sent = sent,
            date = FormatterRepository.sqlDateFormatter.format(date)
        )
    }
}