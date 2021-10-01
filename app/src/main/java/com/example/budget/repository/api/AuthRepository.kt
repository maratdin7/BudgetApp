package com.example.budget.repository.api

import com.example.budget.client.api.BudgetApi

class AuthRepository(private val budgetApi: BudgetApi) {

    suspend fun signIn(email: String, pass: String) = budgetApi.signIn(email, pass)

}