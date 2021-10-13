package com.example.budget.viewModel.dropDownField

import android.content.Context
import android.util.Log
import com.example.budget.dto.CashAccountEntity
import com.example.budget.repository.PersistentRepository
import com.example.budget.repository.api.withDefault.CashAccountRepository
import com.example.budget.viewModel.Event
import retrofit2.Response

class CashAccountViewModel(
    override val repository: CashAccountRepository = CashAccountRepository,
) : AbstractDropDownFieldViewModel<CashAccountEntity>(repository) {

    override fun loadFromPersistent(context: Context): CashAccountEntity? =
        repository.loadFromPersistent<CashAccountEntity>(context)

    override fun saveToPersistent(context: Context, entity: CashAccountEntity) =
        repository.saveToPersistent(context, entity)

    fun createCashAccountEntity(
        groupId: Int,
        name: String,
        cash: Double,
        callback: (Event<CashAccountEntity?>) -> Unit,
    ) {
        requestWithCallback({ repository.createCashAccount(groupId, name, cash) }) { callback(it) }
    }

    override suspend fun getListEntities(groupId: Int): Response<List<CashAccountEntity>> =
        repository.allCashAccounts(groupId)

}