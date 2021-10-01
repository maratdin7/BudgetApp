package com.example.budget.viewModel.wrap

import androidx.lifecycle.MutableLiveData

open class ErrorWrap<T> {
    private val _error: MutableLiveData<T> = MutableLiveData()
    val error: MutableLiveData<T> = MutableLiveData()

    fun setError(value: T?) {
        error.value = value
    }

    fun haveError(): Boolean = error.value != null
}