package com.cikup.sumopod.ai.cache

import android.content.Context
import androidx.room.Room

public fun createSumoPodDatabase(context: Context): SumoPodDatabase =
    Room.databaseBuilder(
        context = context,
        klass = SumoPodDatabase::class.java,
        name = "sumopod_ai.db",
    ).build()
