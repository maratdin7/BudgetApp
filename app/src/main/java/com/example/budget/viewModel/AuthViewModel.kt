package com.example.budget.viewModel

import com.example.budget.client.NetworkService
import com.example.budget.repository.api.AuthRepository

class AuthViewModel : MainViewModel() {

    private val repository = AuthRepository(NetworkService.create("auth/"))

    val email = "maratdin7+1@gmail.com"
    val pass = "i_love_poly"

//    fun signIn(email: String, pass: String, context: Context) {
//        requestWithCallback({ repository.signIn(email, pass) }) {
//            when (it) {
//                is Event.Success -> Toast.makeText(context, "${it.data.accessToken}", Toast.LENGTH_LONG).show()
//                is Event.Error -> {
//                    Toast.makeText(context, "${it.error}", Toast.LENGTH_LONG).show()
//                }
//                Event.Loading -> {
//                }
//            }
//        }
//    }


}