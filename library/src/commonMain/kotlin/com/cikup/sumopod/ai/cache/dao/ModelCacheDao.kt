package com.cikup.sumopod.ai.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cikup.sumopod.ai.cache.entity.CachedModel

@Dao
interface ModelCacheDao {

    @Query("SELECT * FROM cached_models ORDER BY id ASC")
    suspend fun getAll(): List<CachedModel>

    @Query("SELECT * FROM cached_models WHERE id = :modelId")
    suspend fun getById(modelId: String): CachedModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(models: List<CachedModel>)

    @Query("DELETE FROM cached_models")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM cached_models WHERE cachedAt > :sinceTimestamp")
    suspend fun countCachedSince(sinceTimestamp: Long): Int
}
