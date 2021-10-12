package com.example.budget.repository.api

import com.example.budget.client.NetworkService
import com.example.budget.client.api.LocalExchangeApi
import com.example.budget.dto.LocalExchangeEntity
import com.example.budget.repository.FormatterRepository
import retrofit2.Response
import retrofit2.http.Query

object LocalExchangeRepository : IExpenseRepository<LocalExchangeEntity> {

    private val localExchangeApi: LocalExchangeApi = NetworkService.create("localExchange/")

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

    suspend fun getAllLocalExchanges(groupId: Int): Response<List<LocalExchangeEntity>> =
        localExchangeApi.getAllLocalExchanges(groupId)

}