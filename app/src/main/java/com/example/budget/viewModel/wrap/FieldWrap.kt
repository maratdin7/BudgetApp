package com.example.budget.viewModel.wrap

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class FieldWrap<T, E>(private var toStr: (data: T) -> String = { it -> it.toString() }) : ErrorWrap<E?>() {
    private val _data: MutableLiveData<T> = MutableLiveData()
    val data: LiveData<T> = _data

    fun setValue(value: T) {
        _data.value = value
        setError(null)
    }

    fun getOrNull(errorMsg: E): T? = data.value.run {
        if (this == null) setError(errorMsg)
        return this
    }

    fun clear() {
        _data.value = null
    }

    fun getOrDefault(defValue: T): T = data.value ?: defValue

    override fun toString(): String = data.value?.let(toStr) ?: ""
}