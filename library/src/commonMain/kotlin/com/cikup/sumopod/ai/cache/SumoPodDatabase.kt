package com.cikup.sumopod.ai.cache

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.cikup.sumopod.ai.cache.dao.ModelCacheDao
import com.cikup.sumopod.ai.cache.entity.CachedModel

@Database(
    entities = [CachedModel::class],
    version = 1,
    exportSchema = true,
)
@ConstructedBy(SumopodDatabaseConstructor::class)
internal abstract class SumopodDatabase : RoomDatabase() {
    internal abstract fun modelCacheDao(): ModelCacheDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
internal expect object SumopodDatabaseConstructor : RoomDatabaseConstructor<SumopodDatabase>
