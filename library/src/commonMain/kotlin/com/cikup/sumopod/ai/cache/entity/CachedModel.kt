package com.cikup.sumopod.ai.cache.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cached_models")
public data class CachedModel(
    @PrimaryKey val id: String,
    val objectType: String,
    val created: Long?,
    val ownedBy: String?,
    val cachedAt: Long,
)
