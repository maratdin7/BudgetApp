package com.example.budget.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager
import com.example.budget.R
import com.example.budget.dto.CashAccountEntity
import com.example.budget.dto.CategoryEntity
import com.example.budget.dto.GroupEntity
import com.google.gson.Gson

object PersistentRepository {

    val token: String = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtYXJhdGRpbjcrMUBnbWFpbC5jb20iLCJpYXQiOjE2MzQwMDAxOTksImV4cCI6MTYzNDAwMzc5OX0.iREnuEOS-3he2HSOMqdHSu5CenXsnsBIvMPoPsggktbsAzxP7DsW8-qd_z0NoZ_a2fADK8viJZDOCkSmzLFN6Q"

    fun loadDefaultCashAccountId(context: Context): Int? {
        val pref = PreferenceManager.getDefaultSharedPreferences(context)
        val pos = pref.getInt(context.getString(R.string.defaultCashAccount), -1)
        return pos.takeUnless { pos == -1 }
    }

    inline fun <reified T> loadJsonObject(context: Context, key: String): T? {
        val mPrefs = PreferenceManager.getDefaultSharedPreferences(context)
        val json = mPrefs.getString(key, null)

        return Gson().fromJson(json, T::class.java)
    }

    inline fun <reified K, reified T : Map<Int, K>> loadMapJsonObject(context: Context, key: String, keyMap: Int): K? {
        val mapEntity = loadJsonObject<T>(context, key)

        return mapEntity?.get(keyMap)
    }

    inline fun <reified T> saveJsonObject(context: Context, key: String, entity: T) {
        val mPrefs = PreferenceManager.getDefaultSharedPreferences(context)
        mPrefs.edit().also {
            it.putString(key, Gson().toJson(entity))
            it.apply()
        }
    }

    inline fun <reified K> saveMapJsonObject(context: Context, key: String, keyMap: Int, entity: K) {
        val mapEntities = loadJsonObject<MutableMap<Int, K>>(context, key) ?: mutableMapOf()
        mapEntities[keyMap] = entity
        saveJsonObject(context, key, mapEntities)
    }


    inline fun <reified T> setDefEntity(entity: T) {
        when (entity) {
            is GroupEntity -> setGroup(entity)
            is CategoryEntity -> defCategoryEntity = entity
            is CashAccountEntity -> defCashAccountEntity = entity
        }
    }

    fun setGroup(groupEntity: GroupEntity) {
        _defGroupEntity.value = groupEntity
    }

    private val _defGroupEntity: MutableLiveData<GroupEntity> = MutableLiveData()
    val defGroupEntity: LiveData<GroupEntity> = _defGroupEntity

    var defCategoryEntity: CategoryEntity? = null

    var defCashAccountEntity: CashAccountEntity? = null

}