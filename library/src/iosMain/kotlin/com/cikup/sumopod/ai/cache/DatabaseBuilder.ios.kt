package com.cikup.sumopod.ai.cache

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import platform.Foundation.NSHomeDirectory

fun createSumoPodDatabase(): SumoPodDatabase {
    val dbPath = NSHomeDirectory() + "/sumopod_ai.db"
    return Room.databaseBuilder<SumoPodDatabase>(
        name = dbPath,
        factory = SumoPodDatabaseConstructor::initialize,
    )
        .setDriver(BundledSQLiteDriver())
        .build()
}
