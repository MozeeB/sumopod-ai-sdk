package com.cikup.sumopod.ai.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cikup.sumopod.ai.cache.entity.CachedModel

@Dao
public interface ModelCacheDao {

    @Query("SELECT * FROM cached_models ORDER BY id ASC")
    public suspend fun getAll(): List<CachedModel>

    @Query("SELECT * FROM cached_models WHERE id = :modelId")
    public suspend fun getById(modelId: String): CachedModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public suspend fun insertAll(models: List<CachedModel>)

    @Query("DELETE FROM cached_models")
    public suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM cached_models WHERE cachedAt > :sinceTimestamp")
    public suspend fun countCachedSince(sinceTimestamp: Long): Int
}
