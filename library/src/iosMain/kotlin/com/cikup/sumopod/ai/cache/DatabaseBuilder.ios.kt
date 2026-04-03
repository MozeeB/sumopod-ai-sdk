package com.cikup.sumopod.ai.cache

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import platform.Foundation.NSHomeDirectory

internal fun createSumopodDatabase(): SumopodDatabase {
    val dbPath = NSHomeDirectory() + "/sumopod_ai.db"
    return Room.databaseBuilder<SumopodDatabase>(
        name = dbPath,
        factory = SumopodDatabaseConstructor::initialize,
    )
        .setDriver(BundledSQLiteDriver())
        .build()
}
