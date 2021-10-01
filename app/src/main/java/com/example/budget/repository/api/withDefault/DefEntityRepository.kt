package com.example.budget.repository.api.withDefault

import android.content.Context
import com.example.budget.dto.GroupEntity
import com.example.budget.repository.PersistentRepository

abstract class DefEntityRepository<T> {
    var defEntity: T? = null

    abstract fun Context.getPersistentKey(): String

    inline fun <reified K : T> saveToPersistent(context: Context, entity: K) {
        val key = context.getPersistentKey()
        with(PersistentRepository) {

            when (entity) {
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
                K::class.java.isAssignableFrom(GroupEntity::class.java) -> loadJsonObject<K>(context, key)
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
}