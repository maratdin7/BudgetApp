package com.example.budget.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Response

open class MainViewModel : ViewModel() {

//
//    fun <T> requestWithLiveData(
//        liveData: MutableLiveData<Event<T>>,
//        request: suspend () -> Response<T>,
//    ) {
//
//        liveData.postValue(Event.Loading)
//
//        this.viewModelScope.launch(Dispatchers.IO) {
//            try {
//                val response = request.invoke()
//                if (response.data != null) {
//                    liveData.postValue(Event.Success(response.data))
//                } else if (response.error != null) {
//                    liveData.postValue(Event.Error(response.error))
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//                liveData.postValue(Event.Error(e))
//            }
//        }
//    }

//    fun <T> requestWithCallback(
//        request: suspend () -> Response<T>,
//        response: (Event<T>) -> Unit,
//    ) {
//        response(Event.Loading)
//
//        this.viewModelScope.launch(Dispatchers.IO) {
//            try {
//                val res = request.invoke()
//
//                launch(Dispatchers.Main) {
//                    when {
//                        res.data != null -> response(Event.Success(res.data))
//                        res.error != null -> response(Event.Error(res.error))
//                    }
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//                launch(Dispatchers.Main) {
//                    response(Event.Error(e))
//                }
//            }
//        }
//    }

    protected fun <T> requestWithCallback(
        request: suspend () -> Response<T>,
        response: (Event<T?>) -> Unit,
    ) {
        response(Event.Loading)

        this.viewModelScope.launch(Dispatchers.IO) {
            try {
                val res = request.invoke()

                launch(Dispatchers.Main) {
                    when {
                        res.isSuccessful -> response(Event.Success(res.body()))
                        else -> response(Event.Error(res.code(), res.message() ?: ""))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                launch(Dispatchers.Main) {
                    response(Event.Error(-1))
                }
            }
        }
    }
}

sealed class Event<out Any> {
    var isHandled: Boolean = false

    data class Success<T>(val data: T?) : Event<T?>()
    data class Error(val errorCode: Int, val message: String = "") : Event<Nothing>()
    object Loading : Event<Nothing>()
}