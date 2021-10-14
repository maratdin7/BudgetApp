package com.example.budget.client

import com.example.budget.repository.PersistentRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object NetworkService {

    private const val BASE_URL = "http://192.168.0.160:8080/"

    private val loggingInterceptor = run {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.apply {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        }
    }

    private val client: OkHttpClient = OkHttpClient
        .Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor { chain ->
            val newUrl = chain
                .request()
                .url
                .newBuilder()
                .build()

            val token = PersistentRepository.accessToken

            val request = chain
                .request()
                .newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .url(newUrl)
                .build()

            chain.proceed(request)
        }.build()

    fun retrofitService(url: String): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL + url)
        .addConverterFactory(GsonConverterFactory.create())
        .addConverterFactory(ScalarsConverterFactory.create())
        .client(client)
        .build()

    inline fun <reified T> create(url:String): T =
        retrofitService(url)
        .create(T::class.java)



}