package com.cikup.sumopod.ai.cache

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import java.io.File

internal fun createSumopodDatabase(dbPath: String? = null): SumopodDatabase {
    val path = dbPath ?: File(System.getProperty("user.home"), ".sumopod/sumopod_ai.db").also {
        it.parentFile?.mkdirs()
    }.absolutePath
    return Room.databaseBuilder<SumopodDatabase>(
        name = path,
        factory = SumopodDatabaseConstructor::initialize,
    )
        .setDriver(BundledSQLiteDriver())
        .build()
}
