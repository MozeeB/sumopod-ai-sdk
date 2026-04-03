package com.cikup.sumopod.ai.cache

import android.content.Context
import androidx.room.Room

internal fun createSumopodDatabase(context: Context): SumopodDatabase =
    Room.databaseBuilder(
        context = context,
        klass = SumopodDatabase::class.java,
        name = "sumopod_ai.db",
    ).build()
