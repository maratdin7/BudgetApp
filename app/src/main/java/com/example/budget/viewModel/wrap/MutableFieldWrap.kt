package com.example.budget.viewModel.wrap

import androidx.lifecycle.MutableLiveData

class MutableFieldWrap<T, E>(private var toStr: (data: T) -> String = { it -> it.toString() }) : ErrorWrap<E?>() {
    val data: MutableLiveData<T> = MutableLiveData()

    fun setValue(value: T) {
        data.value = value
    }

    fun clear() {
        data.value = null
    }

    override fun toString(): String = data.value?.let(toStr) ?: ""

    init {
        data.observeForever { setError(null) }
    }
}