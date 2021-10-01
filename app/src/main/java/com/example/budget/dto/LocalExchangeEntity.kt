package com.example.budget.dto

import java.util.*

data class LocalExchangeEntity(
    val id: Int = 0,
    val senderId: Int,
    val receiverId: Int,
    val sent: Double,
    val date: Date,
    val comment: String?,
    ) {
    override fun toString(): String =
        "Entity of type: ${javaClass.name} ( " +
                "id = $id " +
                "senderId = $senderId " +
                "reciverId = $receiverId " +
                "sent = $sent " +
                "date = $date " +
                "comment = $comment " +
                ")"

}


