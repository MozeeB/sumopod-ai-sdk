package com.cikup.sumopod.sample

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.cikup.sumopod.sample.di.ClientProvider
import com.cikup.sumopod.sample.navigation.AppNavigation
import com.cikup.sumopod.sample.screen.settings.SettingsViewModel
import com.cikup.sumopod.sample.theme.SumopodTheme

@Composable
fun App() {
    val settingsViewModel = remember { SettingsViewModel() }
    val clientProvider = remember { ClientProvider(settingsViewModel) }

    SumopodTheme {
        AppNavigation(settingsViewModel, clientProvider)
    }
}
