package com.example.budget.viewModel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.budget.R
import com.example.budget.dto.AuthEntity
import com.example.budget.repository.PersistentRepository
import com.example.budget.repository.api.AuthRepository
import com.example.budget.viewModel.wrap.FieldWrap
import com.example.budget.viewModel.wrap.FieldWrapWithError
import com.example.budget.viewModel.wrap.MutableFieldWrap

class AuthViewModel : MainViewModel() {

    private val repository = AuthRepository

    val emailField: MutableFieldWrap<String, String> = MutableFieldWrap()
    val passField: MutableFieldWrap<String, String> = MutableFieldWrap()

    fun checkError(): Boolean = (emailField.isFieldNotEnabled() || passField.isFieldNotEnabled())

    private fun MutableFieldWrap<String, *>.isFieldNotEnabled() = data.value.isNullOrBlank() || haveError()

    fun checkEmailError(email: String, errorStr: String) {
        emailField.error.value = errorStr.takeUnless { email.contains('@') }
    }

    fun checkPassError(pass: String, errorStr: String) {
        passField.error.value = errorStr.takeUnless { pass.length >= 6 }
    }

    val isLoading: MutableFieldWrap<Boolean, String> = MutableFieldWrap()

    fun loadRefreshToken(context: Context): String? = repository.loadFromPersistent<String>(context)

    fun generateAccessToken(
        context: Context,
        email: String?,
        refreshToken: String?,
        callback: (Event<AuthEntity?>) -> Unit,
    ) {
        requestWithCallback({repository.generateAccessToken(email, refreshToken)}) {
            when (it) {
                is Event.Success -> updateAuthTokens(context, it.data!!)
                is Event.Error -> Unit
                Event.Loading -> Unit
            }
            callback(it)
        }
    }

    private fun updateAuthTokens(context: Context, authEntity: AuthEntity) {
        repository.saveToPersistent(context, authEntity.refreshToken)
        PersistentRepository.accessToken = authEntity.accessToken
        PersistentRepository.isLogin.value = true
    }

    private fun Context.saveNewUser(email: String, authEntity: AuthEntity) {
        PersistentRepository.saveJsonObject(this, getString(R.string.user_email), email)
        updateAuthTokens(this, authEntity)
    }

    fun signIn(context: Context, email: String, pass: String, callback: (Event<AuthEntity?>) -> Unit) {
        requestWithCallback({repository.signIn(email, pass)}) {
            when (it) {
                is Event.Success -> {
                    val authEntity = it.data!!
                    context.saveNewUser(email, authEntity)
                }
                is Event.Error -> signUp(context, email, pass, callback)
                Event.Loading -> Unit
            }
            callback(it)
        }
    }

    fun signUp(context: Context, email: String, pass: String, callback: (Event<AuthEntity?>) -> Unit) {
        requestWithCallback({repository.signUp(email, pass)}) {
            when (it) {
                is Event.Success -> {
                    val authEntity = it.data!!
                    context.saveNewUser(email, authEntity)
                }
                is Event.Error -> emailField.setError(context.getString(R.string.userAlreadyExist))
                Event.Loading -> Unit
            }
            callback(it)
        }
    }

}