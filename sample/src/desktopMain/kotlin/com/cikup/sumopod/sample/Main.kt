package com.cikup.sumopod.sample

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.cikup.sumopod.sample.di.sampleModule
import org.koin.core.context.startKoin

fun main() = application {
    try {
        startKoin {
            modules(sampleModule)
        }
    } catch (_: Exception) {
        // Koin already started
    }

    Window(
        onCloseRequest = ::exitApplication,
        title = "SumoPod AI Sample",
    ) {
        App()
    }
}
