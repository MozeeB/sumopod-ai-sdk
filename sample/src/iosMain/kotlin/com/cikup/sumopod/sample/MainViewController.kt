package com.cikup.sumopod.sample

import androidx.compose.ui.window.ComposeUIViewController
import com.cikup.sumopod.sample.di.sampleModule
import org.koin.core.context.startKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        try {
            startKoin {
                modules(sampleModule)
            }
        } catch (_: Exception) {
            // Koin already started
        }
    },
) {
    App()
}
