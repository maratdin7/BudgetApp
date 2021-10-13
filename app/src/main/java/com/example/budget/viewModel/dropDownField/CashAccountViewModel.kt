package com.example.budget.viewModel.dropDownField

import android.content.Context
import android.util.Log
import com.example.budget.dto.CashAccountEntity
import com.example.budget.repository.PersistentRepository
import com.example.budget.repository.api.withDefault.CashAccountRepository
import retrofit2.Response

class CashAccountViewModel(
    override val repository: CashAccountRepository = CashAccountRepository
) : AbstractDropDownFieldViewModel<CashAccountEntity>(repository) {

    override fun loadFromPersistent(context: Context): CashAccountEntity? =
        repository.loadFromPersistent<CashAccountEntity>(context)

    override fun saveToPersistent(context: Context, entity: CashAccountEntity) =
        repository.saveToPersistent(context, entity)

    override suspend fun getListEntities(groupId: Int): Response<List<CashAccountEntity>> =
        repository.allCashAccounts(groupId)
}