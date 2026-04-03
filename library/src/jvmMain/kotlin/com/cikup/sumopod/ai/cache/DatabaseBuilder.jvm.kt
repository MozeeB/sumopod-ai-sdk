package com.cikup.sumopod.ai.cache

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import java.io.File

public fun createSumoPodDatabase(dbPath: String? = null): SumoPodDatabase {
    val path = dbPath ?: File(System.getProperty("user.home"), ".sumopod/sumopod_ai.db").also {
        it.parentFile?.mkdirs()
    }.absolutePath
    return Room.databaseBuilder<SumoPodDatabase>(
        name = path,
        factory = SumoPodDatabaseConstructor::initialize,
    )
        .setDriver(BundledSQLiteDriver())
        .build()
}
