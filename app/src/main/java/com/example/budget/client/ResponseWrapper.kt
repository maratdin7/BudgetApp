package com.example.budget.client

import com.google.gson.annotations.SerializedName

data class ResponseWrapper<T>(
    @SerializedName("response")
    val data: T? = null,
    @SerializedName("error")
    val error: Error? = null,
)

data class ResponseWrapperList<T>(
    @SerializedName("response")
    val data: List<T>? = null,
    @SerializedName("error")
    val error: Error? = null,
)