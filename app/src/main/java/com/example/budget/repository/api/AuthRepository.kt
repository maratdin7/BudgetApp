package com.example.budget.repository.api

import android.content.Context
import com.example.budget.R
import com.example.budget.client.NetworkService
import com.example.budget.client.api.AuthApi
import com.example.budget.dto.AuthEntity
import com.example.budget.dto.UserEntity
import com.example.budget.repository.api.withDefault.DefEntityRepository
import retrofit2.Response

object AuthRepository : DefEntityRepository<String>() {

    private val authApi: AuthApi = NetworkService.create("auth/")

    suspend fun signIn(email: String, pass: String) =
        authApi.signIn(email, pass)

    suspend fun signUp(email: String, pass: String): Response<AuthEntity> =
        authApi.signUp(email, pass)

    suspend fun generateAccessToken(email: String?, refreshToken: String?): Response<AuthEntity> =
        authApi.generateAccessToken(email, refreshToken)

    suspend fun resetPassword(email: String): Response<UserEntity> = authApi.resetPassword(email)

    suspend fun confirmResetPassword(email: String, token: Int): Response<String> =
        authApi.confirmResetPassword(email, token)

    override fun Context.getPersistentKey(): String = getString(R.string.refreshToken)

}