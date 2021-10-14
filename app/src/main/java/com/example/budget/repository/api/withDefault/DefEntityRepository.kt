package com.example.budget.repository.api.withDefault

import android.content.Context
import com.example.budget.dto.AuthEntity
import com.example.budget.dto.GroupEntity
import com.example.budget.repository.PersistentRepository

abstract class DefEntityRepository<T> {
    var defEntity: T? = null

    abstract fun Context.getPersistentKey(): String

    inline fun <reified K : T> saveToPersistent(context: Context, entity: K) {
        val key = context.getPersistentKey()
        with(PersistentRepository) {

            when (entity) {
                is String -> saveJsonObject(context, key, entity)
                is GroupEntity -> saveJsonObject(context, key, entity)
                else -> {
                    val groupId = defGroupEntity.value?.id ?: return
                    saveMapJsonObject(context, key, groupId, entity)
                }
            }

            setDefEntity(entity)
            defEntity = entity
        }
    }

    inline fun <reified K : T> loadFromPersistent(context: Context): T? {
        defEntity?.let { return it }

        val key = context.getPersistentKey()

        with(PersistentRepository) {
            val entity = when  {
                checkType<K, String>() -> loadJsonObject<K>(context, key)
                checkType<K, GroupEntity>() -> loadJsonObject<K>(context, key)
                else -> {
                    val groupId = defGroupEntity.value?.id ?: return null
                    loadMapJsonObject<K, Map<Int, K>>(context, key, groupId)
                }
            }

            setDefEntity(entity)

            defEntity = entity
            return entity
        }
    }

    inline fun <reified K : T, reified Q> checkType() = K::class.java.isAssignableFrom(Q::class.java)
}