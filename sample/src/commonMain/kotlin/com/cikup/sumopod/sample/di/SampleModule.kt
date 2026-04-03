package com.cikup.sumopod.sample.di

import com.cikup.sumopod.ai.Sumopod
import com.cikup.sumopod.sample.screen.settings.SettingsViewModel

class ClientProvider(private val settingsViewModel: SettingsViewModel) {

    fun ensureInitialized(): Boolean {
        val apiKey = settingsViewModel.apiKey.value
        val baseUrl = settingsViewModel.baseUrl.value
        if (apiKey.isBlank() || !apiKey.startsWith("sk-") || apiKey.length < 20) {
            return false
        }
        Sumopod.init(apiKey) { this.baseUrl = baseUrl }
        return true
    }
}
