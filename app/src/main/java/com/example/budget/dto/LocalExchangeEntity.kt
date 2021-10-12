package com.example.budget.dto

import java.util.*

data class LocalExchangeEntity(
    override var id: Int = 0,
    val senderId: Int,
    val receiverId: Int,
    val sent: Double,
    override val date: Date,
    val comment: String?,
    var refCashAccountEntitySend: CashAccountEntity? = null,
    var refCashAccountEntityReceive: CashAccountEntity? = null,
    ): IDateEntity, IEntity {
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


