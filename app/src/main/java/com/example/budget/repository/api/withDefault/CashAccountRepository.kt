package com.example.budget.repository.api.withDefault

import android.content.Context
import android.util.Log
import com.example.budget.R
import com.example.budget.client.NetworkService
import com.example.budget.client.api.CashAccountApi
import com.example.budget.dto.CashAccountEntity
import retrofit2.Response

object CashAccountRepository: DefEntityRepository<CashAccountEntity>() {

    private val cashAccountApi: CashAccountApi = NetworkService.create("cashAccount/")

    suspend fun createCashAccount(
        groupId: Int,
        name: String,
        cash: Double,
    ): Response<Unit> =
        cashAccountApi.createCashAccount(groupId, name, cash)

    suspend fun allCashAccounts(
        groupId: Int,                                     
    ): Response<List<CashAccountEntity>> =
        cashAccountApi.allCashAccounts(groupId)

    suspend fun getCashAccount(
        groupId: Int,
        cashAccountId: Int,
    ): Response<CashAccountEntity> =
        cashAccountApi.getCashAccount(groupId, cashAccountId)

    override fun Context.getPersistentKey(): String = getString(R.string.defaultCashAccount)

}